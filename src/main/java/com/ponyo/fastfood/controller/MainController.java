package com.ponyo.fastfood.controller;

import com.ponyo.fastfood.service.EmbeddingService;
import com.ponyo.fastfood.service.MainService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8888"}, allowCredentials= "true")
public class MainController {
    private final MainService service;
    private final EmbeddingService embeddingService;

//    @Scheduled(cron = "0 0 0 * * *")
    @GetMapping("/test")
    public ResponseEntity test(){
        HashMap<String, Object> data = service.crawlFromKakao();
        // 2) 벡터 임베딩 & 적재 : 메뉴, 시설정보, 리뷰
        embeddingService.embedding(data);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity getStoreList(HttpServletRequest req){
        // 검색/키워드 조건 + 현재 위치 매칭
        // 1) 현재 위치에 매칭되는 음식점 조회
        // 2) 해당 음식점 목록 중 검색/키워드 조건에 매칭되는 음식점 조회
        String latlong = req.getParameter("latlon");
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
