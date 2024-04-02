package com.ponyo.fastfood.service;

import com.ponyo.fastfood.domain.mongo.PlaceDTO;
import com.ponyo.fastfood.domain.mongo.ReviewDTO;
import com.ponyo.fastfood.repository.mongo.PlaceRepository;
import com.ponyo.fastfood.repository.mongo.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingServiceImpl implements EmbeddingService{

    private final EmbeddingClient embeddingClient;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public void embedding(HashMap<String, Object> data) {
        List<PlaceDTO> placeList = (List<PlaceDTO>) data.get("placeList");
        List<ReviewDTO> reviewList = (List<ReviewDTO>) data.get("reviewList");
        for(PlaceDTO place : placeList){
            List<Double> menuVector = embeddingClient.embed(place.getMenu());
            double[] menuEmb = menuVector.stream().mapToDouble(Double::doubleValue).toArray();
            List<Double> placeInfoVector = embeddingClient.embed(place.getPlaceInfo());
            double[] placeInfoEmb = placeInfoVector.stream().mapToDouble(Double::doubleValue).toArray();

            place.setMenuEmb(menuEmb);
            place.setPlaceInfoEmb(placeInfoEmb);

            place = placeRepository.insert(place);
            log.info("place id:{}, place name: {}", place.get_id(), place.getName());
        }
        for(ReviewDTO review: reviewList){
            if(review.getReview().trim().length() > 0){
                log.info("review : {}",review.getReview());
                List<Double> reviewVector = embeddingClient.embed(review.getReview());
                double[] reviewEmb = reviewVector.stream().mapToDouble(Double::doubleValue).toArray();
                review.setReviewEmb(reviewEmb);
                review = reviewRepository.insert(review);
                log.info("review id:{}, review place id: {}, review: {}", review.get_id(), review.getPlaceId(), review.getReview());
            }
        }
        log.info("**embedding success**");
    }
}
