package com.ponyo.fastfood.domain;

import lombok.Data;
import lombok.ToString;

@Data
public class PlaceDTO {
    String id;
    String name;
    String addr;
    String tel;
    String degree;
    String menu;
    String placeInfo;
    String period;
    String latlon;

    @ToString.Exclude
    double[] menuEmb;
    @ToString.Exclude
    double[] placeInfoEmb;
}
