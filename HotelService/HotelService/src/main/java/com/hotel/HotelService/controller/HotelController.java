package com.hotel.HotelService.controller;

import com.hotel.HotelService.entity.Hotel;
import com.hotel.HotelService.service.HotelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
    private HotelServiceImpl hotelServiceImpl;

    @PostMapping
    public ResponseEntity<Hotel> create(@RequestBody Hotel hotel) {
        Hotel hotel1 = hotelServiceImpl.create(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(hotel1);
    }

    @GetMapping("/allHotel")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> allHotel = hotelServiceImpl.getAllHotel();
        return ResponseEntity.status(HttpStatus.OK).body(allHotel);
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> getHotelsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(hotelServiceImpl.getHotelsByLocation(location));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Hotel> getUser(@PathVariable String userId) {
        Hotel h = hotelServiceImpl.get(userId);
        return ResponseEntity.ok(h);
    }
}
