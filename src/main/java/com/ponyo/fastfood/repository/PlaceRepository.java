package com.ponyo.fastfood.repository;

import com.ponyo.fastfood.domain.PlaceDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends MongoRepository<PlaceDTO, String> {

}
