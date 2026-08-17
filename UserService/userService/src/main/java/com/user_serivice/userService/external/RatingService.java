package com.user_serivice.userService.external;

import com.user_serivice.userService.entity.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "RATING-SERVICE")
public interface RatingService {
    @GetMapping("/rating/user/{id}")
    List<Rating> getUser(@PathVariable String id);
    @PostMapping("/rating")
    Rating postUser(Rating rating);
}
