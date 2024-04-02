package com.ponyo.fastfood.domain.mongo;

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
    String menu;
    String placeInfo;
    String period;
    @NotBlank
    String latlon;

    @NotBlank
    @ToString.Exclude
    double[] menuEmb;

    @NotBlank
    @ToString.Exclude
    double[] placeInfoEmb;
}
