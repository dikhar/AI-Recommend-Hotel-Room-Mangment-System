package com.rating.RatingService.entity;

/** Stored as a readable string in MongoDB, never as an ordinal number. */
public enum RatingValue {
    NOT_RATED,
    POOR,
    AVERAGE,
    GOOD,
    EXCELLENT
}
