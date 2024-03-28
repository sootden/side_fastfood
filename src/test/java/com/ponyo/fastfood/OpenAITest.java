package com.ponyo.fastfood;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
public class OpenAITest {
    @Autowired
    private EmbeddingClient embeddingClient;

    @Test
    public void test(){
//        System.out.println(embeddingClient.toString());
//        EmbeddingResponse embeddingResponse = embeddingClient.call(
//                new EmbeddingRequest(List.of("Hello World", "World is big and salvation is near"),
//                        OpenAiEmbeddingOptions.builder()
//                                .withModel("Different-Embedding-Model-Deployment-Name")
//                                .build()));
    }
}
