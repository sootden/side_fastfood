package com.ponyo.fastfood.domain.es;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "fastfood.fastfoodreviewinfo", createIndex = false)
public class ReviewDocument {
    @Id
    String _id;
    @Field(type = FieldType.Text, name = "placeId")
    String placeId;
    @Field(type = FieldType.Text, name = "user")
    String user;
    @Field(type = FieldType.Text, name = "review")
    String review;
    @Field(type = FieldType.Text, name = "rating")
    String rating;
    @Field(type = FieldType.Text, name = "date")
    String date;

    @ToString.Exclude
    @Field(type = FieldType.Double, name = "reviewEmb")
    double[] reviewEmb;
}
