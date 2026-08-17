package com.hotel.HotelService.Repository;

import com.hotel.HotelService.entity.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RoomRepository extends MongoRepository<Room, String> {
    List<Room> findByAvailableTrue();
    List<Room> findByHotelIdAndAvailableTrue(String hotelId);
}
