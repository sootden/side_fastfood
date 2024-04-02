package com.ponyo.fastfood.repository.es;

import com.ponyo.fastfood.domain.es.ReviewDocument;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ESRepository extends ElasticsearchRepository<ReviewDocument, String> {
    SearchHits<ReviewDocument> findByPlaceId(String placeId);
}
