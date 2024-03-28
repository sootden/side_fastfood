package com.ponyo.fastfood.service;

import com.ponyo.fastfood.domain.PlaceDTO;
import com.ponyo.fastfood.domain.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingServiceImpl implements EmbeddingService{

    private final EmbeddingClient embeddingClient;


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
        }
        for(ReviewDTO review: reviewList){
            List<Double> reviewVector = embeddingClient.embed(review.getReview());
            double[] reviewEmb = reviewVector.stream().mapToDouble(Double::doubleValue).toArray();
            review.setReviewEmb(reviewEmb);
        }
    }
}
