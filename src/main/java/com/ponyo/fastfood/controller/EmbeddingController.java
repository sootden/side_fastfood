package com.ponyo.fastfood.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class EmbeddingController {
    private final EmbeddingClient embeddingClient;

    @GetMapping("/ai/embedding")
    public ResponseEntity embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//        EmbeddingResponse embeddingResponse = embeddingClient.embedForResponse(List.of(message));
        List<Double> result = embeddingClient.embed(message);
        return new ResponseEntity<>(Map.of("embedding", result), HttpStatus.OK);
    }
}
