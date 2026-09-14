package com.user_serivice.userService.service;

import com.google.genai.Client;
import com.google.genai.gaos.models.interactions.CreateModelInteraction;
import com.google.genai.gaos.models.interactions.Interaction;
import com.google.genai.gaos.models.interactions.InteractionsInput;
import com.google.genai.gaos.models.interactions.Model;
import com.google.genai.gaos.models.interactions.ModelOutputStep;
import com.google.genai.gaos.models.interactions.TextContent;
import com.google.genai.gaos.models.operations.CreateInteractionRequestBody;
import com.user_serivice.userService.dto.Recommendation;
import com.user_serivice.userService.dto.HotelRoomSelection;
import com.user_serivice.userService.entity.Rating;
import com.user_serivice.userService.entity.Room;
import com.user_serivice.userService.external.HotelService;
import com.user_serivice.userService.external.RatingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);
    private final RatingService ratingService;
    private final HotelService hotelService;
    private final String apiKey;
    private final String model;
    private final boolean aiEnabled;

    public RecommendationService(RatingService ratingService, HotelService hotelService,
                                 @Value("${gemini.api-key:}") String apiKey,
                                 @Value("${gemini.model:gemini-3.8-flash}") String model,
                                 @Value("${recommendation.ai-enabled:true}") boolean aiEnabled) {
        this.ratingService = ratingService;
        this.hotelService = hotelService;
        this.apiKey = apiKey;
        this.model = model;
        this.aiEnabled = aiEnabled;
    }

    public List<Recommendation> recommend(String userId, int limit) {
        List<Rating> history = ratingService.getUser(userId);
        Set<String> previouslyRatedRoomIds = history.stream().map(Rating::getRoomId).filter(StringUtils::hasText).collect(Collectors.toSet());
        List<Room> candidates = hotelService.getAvailableRooms().stream()
                .filter(room -> !previouslyRatedRoomIds.contains(room.getRoomId()))
                .sorted(Comparator.comparingInt((Room room) -> relevance(room, history)).reversed())
                .limit(Math.min(limit, 10))
                .toList();

        String aiReason = generateReason(history, candidates);
        boolean fromAi = StringUtils.hasText(aiReason);
        List<Recommendation> recommendations = new ArrayList<>();
        for (Room room : candidates) {
            String fallback = "Suggested because its room type and amenities match themes in your previous feedback.";
            recommendations.add(new Recommendation(room, fromAi ? aiReason : fallback, fromAi));
        }
        return recommendations;
    }

    /** Returns the first room to display once the user has selected a hotel. */
    public Recommendation recommendForHotel(String userId, String hotelId) {
        return recommendForRooms(userId, hotelService.getAvailableRoomsForHotel(hotelId));
    }

    /** Single API payload for the hotel-details page: recommended room is also first in rooms. */
    public HotelRoomSelection roomsForHotel(String userId, String hotelId) {
        List<Room> availableRooms = hotelService.getAvailableRoomsForHotel(hotelId);
        Recommendation recommendedRoom = recommendForRooms(userId, availableRooms);
        if (recommendedRoom == null) return new HotelRoomSelection(null, availableRooms);
        List<Room> orderedRooms = new ArrayList<>();
        orderedRooms.add(recommendedRoom.getRoom());
        availableRooms.stream()
                .filter(room -> !room.getRoomId().equals(recommendedRoom.getRoom().getRoomId()))
                .forEach(orderedRooms::add);
        return new HotelRoomSelection(recommendedRoom, orderedRooms);
    }

    private Recommendation recommendForRooms(String userId, List<Room> availableRooms) {
        List<Rating> history = ratingService.getUser(userId);
        Set<String> ratedRoomIds = history.stream().map(Rating::getRoomId)
                .filter(StringUtils::hasText).collect(Collectors.toSet());
        List<Room> candidates = availableRooms.stream()
                .filter(room -> !ratedRoomIds.contains(room.getRoomId()))
                .sorted(Comparator.comparingInt((Room room) -> relevance(room, history)).reversed())
                .limit(10)
                .toList();
        if (candidates.isEmpty()) return null;

        AiChoice choice = chooseRoomWithAi(history, candidates);
        Room selected = candidates.get(0);
        if (choice != null) {
            for (Room candidate : candidates) {
                if (candidate.getRoomId().equals(choice.roomId())) {
                    return new Recommendation(candidate, choice.reason(), true);
                }
            }
        }
        return new Recommendation(selected,
                "Suggested because its room type and amenities match themes in your previous feedback.", false);
    }

    private int relevance(Room room, List<Rating> history) {
        Set<String> pastTerms = history.stream()
                .map(Rating::getFeedback)
                .filter(StringUtils::hasText)
                .flatMap(feedback -> tokenize(feedback).stream())
                .collect(Collectors.toSet());
        return (int) tokenize(room.getRoomType() + " " + room.getAmenities() + " " + room.getCapacity()).stream()
                .filter(pastTerms::contains)
                .count();
    }

    private Set<String> tokenize(String text) {
        if (!StringUtils.hasText(text)) return Set.of();
        Set<String> terms = new HashSet<>();
        for (String term : text.toLowerCase().split("[^a-z0-9]+")) {
            if (term.length() > 2) terms.add(term);
        }
        return terms;
    }

    private String generateReason(List<Rating> history, List<Room> candidates) {
        if (!aiEnabled || !StringUtils.hasText(apiKey) || candidates.isEmpty()) return null;

        try {
            String prompt = "You recommend hotel rooms from a supplied catalog. Write one short, factual reason (max 30 words) "
                    + "for why the room choices fit the guest's past feedback. Do not invent amenities. "
                    + "Past feedback: " + history.stream().map(Rating::getFeedback).filter(StringUtils::hasText).toList()
                    + ". Candidates: " + candidates.stream().map(r -> r.getRoomType() + " | " + r.getAmenities() + " | capacity " + r.getCapacity()).toList();
            return callGeminiApi(prompt);
        } catch (Exception exception) {
            log.warn("Gemini could not generate a recommendation reason: {}", exception.getMessage());
            return null;
        }
    }

    private AiChoice chooseRoomWithAi(List<Rating> history, List<Room> candidates) {
        if (!aiEnabled || !StringUtils.hasText(apiKey)) return null;
        try {
            String prompt = "Choose exactly one room ID from this available-room catalog using the guest's past feedback. "
                    + "Do not invent amenities. Respond only as ROOM_ID|reason, with a reason of at most 25 words. "
                    + "Feedback: " + history.stream().map(Rating::getFeedback).filter(StringUtils::hasText).toList()
                    + ". Rooms: " + candidates.stream().map(r -> r.getRoomId() + " | " + r.getRoomType() + " | "
                    + r.getAmenities() + " | capacity " + r.getCapacity()).toList();
            String response = callGeminiApi(prompt);
            if (!StringUtils.hasText(response)) return null;
            Matcher matcher = Pattern.compile("^\\s*([^|\\s]+)\\s*\\|\\s*(.+?)\\s*$", Pattern.DOTALL).matcher(response);
            return matcher.matches() ? new AiChoice(matcher.group(1), matcher.group(2)) : null;
        } catch (Exception exception) {
            log.warn("Gemini could not choose a room: {}", exception.getMessage());
            return null;
        }
    }

    private String callGeminiApi(String prompt) {
        Client client = Client.builder().apiKey(apiKey).build();
        CreateModelInteraction params = CreateModelInteraction.builder()
                .model(Model.of(model))
                .input(InteractionsInput.of(prompt))
                .build();
        Interaction interaction = client.interactions
                .create(CreateInteractionRequestBody.of(params))
                .interaction()
                .get();
        return interaction.outputText().orElseGet(() -> interaction.steps().stream()
                .flatMap(List::stream)
                .filter(ModelOutputStep.class::isInstance)
                .map(ModelOutputStep.class::cast)
                .flatMap(step -> step.content().stream().flatMap(List::stream))
                .filter(TextContent.class::isInstance)
                .map(TextContent.class::cast)
                .map(text -> text.text().orElse(""))
                .filter(StringUtils::hasText)
                .collect(Collectors.joining("\n")));
    }

    private record AiChoice(String roomId, String reason) { }
}
