package com.ponyo.fastfood.domain.mongo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "fastfoodReviewInfo")
public class ReviewDTO {
    @Id
    String _id;
    @NotBlank
    String placeId;
    String user;
    String review;
    String rating;
    String date;

    @ToString.Exclude
    double[] reviewEmb;
}
