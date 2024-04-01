package com.ponyo.fastfood.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "fastfoodReviewInfo")
public class ReviewDTO {
    @Id
    String _id;
    @NotBlank
    String placeId;
    String user;
    @Transient
    String review;
    String rating;
    String date;

    @ToString.Exclude
    @Field(name="review")
    double[] reviewEmb;
}
