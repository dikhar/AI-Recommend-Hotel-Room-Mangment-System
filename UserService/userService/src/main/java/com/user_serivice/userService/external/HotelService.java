package com.user_serivice.userService.external;

import com.user_serivice.userService.entity.Hotel;
import com.user_serivice.userService.entity.Rating;
import com.user_serivice.userService.entity.Room;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "HOTEL-SERVICE")
public interface HotelService {
    @GetMapping("/hotel/{id}")
    Hotel getHotelId(@PathVariable String id);

    @GetMapping("/hotel/allHotel")
    List<Hotel> getAllHotels();

    @GetMapping("/rooms/available")
    List<Room> getAvailableRooms();

    @GetMapping("/rooms/hotel/{hotelId}")
    List<Room> getAvailableRoomsForHotel(@PathVariable String hotelId);
}

