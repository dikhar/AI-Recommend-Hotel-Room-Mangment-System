package com.user_serivice.userService.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {
    private String hotelId;
    private String name;
    private String location;
    private String about;
}
