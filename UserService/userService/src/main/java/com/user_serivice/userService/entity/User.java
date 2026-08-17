package com.user_serivice.userService.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String user_Id;
    private String hotel_id;
    private String rating_id;
    private String name;
    private String mail;
    private String about;
    @Transient
    private List<Rating> ratings=new ArrayList<>();

}
