package com.hotel.HotelService.service;

import com.hotel.HotelService.entity.Hotel;

import java.util.List;

public interface HotelSerive {
    Hotel create(Hotel hotel);
    List<Hotel> getAllHotel();
    List<Hotel> getHotelsByLocation(String location);
    Hotel get(String Id);
}
