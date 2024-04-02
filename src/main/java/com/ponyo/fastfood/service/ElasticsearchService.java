package com.ponyo.fastfood.service;

import java.util.Map;

public interface ElasticsearchService {
    Map<String, Object> searchReview(String user);
}
