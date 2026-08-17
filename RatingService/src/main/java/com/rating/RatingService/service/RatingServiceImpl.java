package com.rating.RatingService.service;


import com.rating.RatingService.entity.Rating;
import com.rating.RatingService.entity.RatingValue;
import com.rating.RatingService.repo.Repositoryi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RatingServiceImpl implements RatingServic {
    @Autowired
    private Repositoryi repository;
    @Override
    public Rating saveAllRate(Rating rating) {
        if (rating.getId() == null || rating.getId().isBlank()) {
            rating.setId(UUID.randomUUID().toString());
        }
        if (rating.getRating() == null) {
            rating.setRating(RatingValue.NOT_RATED);
        }
        return repository.save(rating);
    }

    @Override
    public Rating checkIn(String userId, String hotelId, String roomId) {
        Rating rating = new Rating();
        rating.setUserId(userId);
        rating.setHotelId(hotelId);
        rating.setRoomId(roomId);
        rating.setRating(RatingValue.NOT_RATED);
        return saveAllRate(rating);
    }

    @Override
    public List<Rating> getAllRate() {
        return repository.findAll();
    }

    @Override
    public Rating getRate(String Id) {
        return repository.findById(Id).orElseThrow();
    }

    @Override
    public List<Rating> getHotelId(String HotelId) {
        return repository.findByHotelId(HotelId);
    }

    @Override
    public List<Rating> getUserId(String UserId) {
        return repository.findByUserId(UserId);
    }
}
