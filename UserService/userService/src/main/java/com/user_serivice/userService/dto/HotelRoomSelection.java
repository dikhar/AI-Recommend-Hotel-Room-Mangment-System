package com.user_serivice.userService.dto;

import com.user_serivice.userService.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HotelRoomSelection {
    private final Recommendation recommendedRoom;
    private final List<Room> rooms;
}
