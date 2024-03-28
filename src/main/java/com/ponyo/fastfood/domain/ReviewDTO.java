package com.ponyo.fastfood.domain;

import lombok.Data;
import lombok.ToString;

@Data
public class ReviewDTO {
    String id;
    String placeId;
    String user;
    String review;
    String rating;
    String date;

    @ToString.Exclude
    double[] reviewEmb;
}
