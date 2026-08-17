package com.user_serivice.userService.dto;

import com.user_serivice.userService.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Recommendation {
    private final Room room;
    private final String reason;
    private final boolean aiGeneratedReason;
}
