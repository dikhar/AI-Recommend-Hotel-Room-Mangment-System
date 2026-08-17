package com.user_serivice.userService.repository;

import com.user_serivice.userService.entity.User;
//import com.user_serivice.userService.entity.user;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Repo extends MongoRepository<User,String> {

}
