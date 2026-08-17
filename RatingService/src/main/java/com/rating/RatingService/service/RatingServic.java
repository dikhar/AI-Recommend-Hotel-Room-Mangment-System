package com.rating.RatingService.service;

import com.rating.RatingService.entity.Rating;

import java.util.List;

public interface RatingServic {
    Rating saveAllRate(Rating rating);

    Rating checkIn(String userId, String hotelId, String roomId);

    List<Rating> getAllRate();

    Rating getRate(String Id);

    List<Rating> getUserId(String UserId);

    List<Rating> getHotelId(String HotelId);
}
