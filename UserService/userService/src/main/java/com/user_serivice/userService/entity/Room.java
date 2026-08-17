package com.user_serivice.userService.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private String roomId;
    private String hotelId;
    private String roomNumber;
    private String roomType;
    private String amenities;
    private Double pricePerNight;
    private Integer capacity;
    private boolean available;
}
