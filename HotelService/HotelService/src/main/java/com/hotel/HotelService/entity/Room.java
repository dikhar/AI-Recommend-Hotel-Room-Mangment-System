package com.hotel.HotelService.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    private String roomId;

    private String hotelId;

    private String roomNumber;

    private String roomType;
    private String amenities;
    private Double pricePerNight;
    private Integer capacity;
    private boolean available = true;
}
