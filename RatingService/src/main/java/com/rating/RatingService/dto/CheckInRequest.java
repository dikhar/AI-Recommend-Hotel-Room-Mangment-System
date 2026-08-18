package com.rating.RatingService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckInRequest {
    private String userId;
    private String hotelId;
    private String roomId;
}
