package com.rating.RatingService.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ratings")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    private String id;

    private String userId;
    private String hotelId;

    private String roomId;

    private RatingValue rating = RatingValue.NOT_RATED;

    private String feedback;

    // Keeps clients that do not yet send a room ID compatible with the existing API.
    public Rating(String id, String userId, String hotelId, String feedback) {
        this.id = id;
        this.userId = userId;
        this.hotelId = hotelId;
        this.feedback = feedback;
        this.rating = RatingValue.NOT_RATED;
    }

}
