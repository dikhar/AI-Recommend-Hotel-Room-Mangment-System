package com.hotel.HotelService.Repository;

import com.hotel.HotelService.entity.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface Repo extends MongoRepository<Hotel,String> {
    List<Hotel> findByLocationIgnoreCase(String location);
}
