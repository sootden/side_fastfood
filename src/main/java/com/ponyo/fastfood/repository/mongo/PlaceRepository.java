package com.ponyo.fastfood.repository.mongo;

import com.ponyo.fastfood.domain.mongo.PlaceDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends MongoRepository<PlaceDTO, String> {
}
