package com.rating.RatingService.repo;

import com.rating.RatingService.entity.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface Repositoryi extends MongoRepository<Rating,String> {
    List<Rating> findByUserId(String UserId);

    List<Rating> findByHotelId(String HotelId);
}
