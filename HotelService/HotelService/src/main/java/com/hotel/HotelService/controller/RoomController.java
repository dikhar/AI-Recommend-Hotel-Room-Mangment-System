package com.hotel.HotelService.controller;

import com.hotel.HotelService.Repository.RoomRepository;
import com.hotel.HotelService.entity.Room;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @PostMapping
    public ResponseEntity<Room> create(@RequestBody Room room) {
        room.setRoomId(UUID.randomUUID().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(roomRepository.save(room));
    }

    @GetMapping("/available")
    public List<Room> availableRooms() {
        return roomRepository.findByAvailableTrue();
    }

    @GetMapping("/hotel/{hotelId}")
    public List<Room> availableRoomsForHotel(@PathVariable String hotelId) {
        return roomRepository.findByHotelIdAndAvailableTrue(hotelId);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Room> get(@PathVariable String roomId) {
        return roomRepository.findById(roomId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
