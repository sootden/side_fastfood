package com.ponyo.fastfood.controller;

import com.ponyo.fastfood.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ElasticsearchController {
    private final ElasticsearchService elasticsearchService;

    @GetMapping("/es/reviewSearch")
    public ResponseEntity search(@RequestParam(value = "place", defaultValue = "place_1_1") String placeId){
        Map<String, Object> result = elasticsearchService.searchReview(placeId);
        return new ResponseEntity<>(Map.of("embedding", result), HttpStatus.OK);
    }
}
