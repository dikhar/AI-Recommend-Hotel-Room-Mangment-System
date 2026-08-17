package com.user_serivice.userService.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    private String ratingId;
    private String UserId;
    private String HotelId;
    private String roomId;
    private RatingValue rating = RatingValue.NOT_RATED;
    private String feedback;
    private Hotel hotelListl;
}
