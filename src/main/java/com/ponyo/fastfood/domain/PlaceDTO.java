package com.ponyo.fastfood.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@Document(collection = "fastfoodPlaceInfo")
public class PlaceDTO {
    @Id
    String _id;
    @NotBlank
    String placeId;
    @NotBlank
    String name;
    @NotBlank
    String addr;
    String tel;
    String degree;
    @Transient
    String menu;
    @Transient
    String placeInfo;
    String period;
    @NotBlank
    String latlon;

    @NotBlank
    @Field(name="menu")
    @ToString.Exclude
    double[] menuEmb;

    @NotBlank
    @Field(name="placeInfo")
    @ToString.Exclude
    double[] placeInfoEmb;
}
