package com.ponyo.fastfood.repository;

import com.ponyo.fastfood.domain.ReviewDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends MongoRepository<ReviewDTO, String> {
}
