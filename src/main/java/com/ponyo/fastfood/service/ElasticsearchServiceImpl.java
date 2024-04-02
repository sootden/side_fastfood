package com.ponyo.fastfood.service;

import com.ponyo.fastfood.domain.es.ReviewDocument;
import com.ponyo.fastfood.repository.es.ESRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ElasticsearchServiceImpl implements ElasticsearchService{
    private final ESRepository esRepository;

    @Override
    public Map<String, Object> searchReview(String placeId) {
        SearchHits<ReviewDocument> searchHits = esRepository.findByPlaceId(placeId);
        Map<String, Object> result = new HashMap<>();
        result.put("count", searchHits.getTotalHits());
        List<ReviewDocument> ReviewList = new ArrayList<>();
        for(SearchHit<ReviewDocument> hit : searchHits) {
            ReviewDocument dto =  (ReviewDocument) hit.getContent();
            ReviewList.add(dto);
        }
        result.put("data", ReviewList);
        return result;
    }
}
