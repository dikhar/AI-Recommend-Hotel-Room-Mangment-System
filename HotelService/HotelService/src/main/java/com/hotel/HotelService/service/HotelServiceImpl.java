package com.hotel.HotelService.service;

import com.hotel.HotelService.Repository.Repo;
import com.hotel.HotelService.entity.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HotelServiceImpl implements HotelSerive{
    @Autowired
    private Repo repo;
    @Override
    public Hotel create(Hotel hotel) {
        String uuid= UUID.randomUUID().toString();
        hotel.setHotelId(uuid);
        return repo.save(hotel);
    }
    @Override
    public List<Hotel> getAllHotel() {
        return repo.findAll();
    }

    @Override
    public List<Hotel> getHotelsByLocation(String location) {
        return repo.findByLocationIgnoreCase(location);
    }

    @Override
    public Hotel get(String Id) {
        return repo.findById(Id).orElseThrow();
    }
}
