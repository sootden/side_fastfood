package com.ponyo.fastfood.domain.es;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "fastfood.fastfoodplaceinfo", createIndex = false)
public class PlaceDocument {
    @Id
    String _id;
    @Field(type = FieldType.Text, name = "placeId")
    String placeId;
    @Field(type = FieldType.Text, name = "name")
    String name;
    @Field(type = FieldType.Text, name = "addr")
    String addr;
    @Field(type = FieldType.Text, name = "tel")
    String tel;
    @Field(type = FieldType.Text, name = "degree")
    String degree;
    @Field(type = FieldType.Text, name = "menu")
    String menu;
    @Field(type = FieldType.Text, name = "placeInfo")
    String placeInfo;
    @Field(type = FieldType.Text, name = "period")
    String period;
    @Field(type = FieldType.Text, name = "latlon")
    String latlon;

    @ToString.Exclude
    @Field(type = FieldType.Double, name = "menuEmb")
    double[] menuEmb;
    @ToString.Exclude
    @Field(type = FieldType.Double, name = "placeInfoEmb")
    double[] placeInfoEmb;
}
