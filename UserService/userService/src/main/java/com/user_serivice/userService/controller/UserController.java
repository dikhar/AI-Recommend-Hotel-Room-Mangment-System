package com.user_serivice.userService.controller;

import com.user_serivice.userService.config.MyConfiguration;
import com.user_serivice.userService.entity.User;
import com.user_serivice.userService.dto.Recommendation;
import com.user_serivice.userService.dto.HotelRoomSelection;
import com.user_serivice.userService.service.RecommendationService;
import com.user_serivice.userService.services.impl.UserServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user)
    {
        User user1=userServiceImpl.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }
    @RequestMapping("/getAll")
    public ResponseEntity<List<User>> getUser()
    {
        List<User> alluser=userServiceImpl.getAllUser();
        return ResponseEntity.ok(alluser);
    }
    @RequestMapping("/{userId}")
    @CircuitBreaker(name = "ratingHotelBreaker",fallbackMethod = "ratingHotelFallBack")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId)
    {
        User user=userServiceImpl.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/recommendations")
    public ResponseEntity<List<Recommendation>> recommendations(@PathVariable String userId,
                                                                  @RequestParam(defaultValue = "5") int limit) {
        if (limit < 1 || limit > 10) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(recommendationService.recommend(userId, limit));
    }

    @GetMapping("/{userId}/hotels/{hotelId}/recommended-room")
    public ResponseEntity<Recommendation> recommendedRoomForHotel(@PathVariable String userId,
                                                                    @PathVariable String hotelId) {
        Recommendation recommendation = recommendationService.recommendForHotel(userId, hotelId);
        return recommendation == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(recommendation);
    }

    @GetMapping("/{userId}/hotels/{hotelId}/rooms")
    public ResponseEntity<HotelRoomSelection> roomsForHotel(@PathVariable String userId,
                                                              @PathVariable String hotelId) {
        return ResponseEntity.ok(recommendationService.roomsForHotel(userId, hotelId));
    }

}
