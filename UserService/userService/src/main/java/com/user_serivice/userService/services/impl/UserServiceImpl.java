package com.user_serivice.userService.services.impl;

import com.user_serivice.userService.config.MyConfiguration;
import com.user_serivice.userService.entity.Hotel;
import com.user_serivice.userService.entity.Rating;
import com.user_serivice.userService.entity.User;
import com.user_serivice.userService.external.HotelService;
import com.user_serivice.userService.external.RatingService;
import com.user_serivice.userService.repository.Repo;
import com.user_serivice.userService.services.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements userService {
    @Autowired
    private Repo repo;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private RatingService ratingService;
    @Override
    public User saveUser(User user) {
        String random= UUID.randomUUID().toString();
        user.setUser_Id(random);
        return repo.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return repo.findAll();
    }

    @Override
    public User getUser(String userId) {
        User user=repo.findById(userId).orElseThrow();
        Rating[] ratingofUser = restTemplate.getForObject("http://localhost:8081/rating/user/"+user.getUser_Id(), Rating[].class);

//        List<Rating> ratingofUser = ratingService.getUser(user.getUser_Id());
        List<Rating> ratings = Arrays.stream(ratingofUser).toList();


        List<Rating> ratingList=ratings.stream().map(rating -> {
            Hotel hotel = hotelService.getHotelId(rating.getHotelId());
            rating.setHotelListl(hotel);
            return rating;
        }).collect(Collectors.toList());
        user.setRatings(ratingList);
        return user;
    }
}
