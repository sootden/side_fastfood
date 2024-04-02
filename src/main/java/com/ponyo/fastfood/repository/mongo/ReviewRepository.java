package com.ponyo.fastfood.repository.mongo;

import com.ponyo.fastfood.domain.mongo.ReviewDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends MongoRepository<ReviewDTO, String> {
}
