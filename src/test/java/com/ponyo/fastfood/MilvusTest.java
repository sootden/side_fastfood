package com.ponyo.fastfood;

import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.DataType;
import io.milvus.param.ConnectParam;
import io.milvus.param.collection.*;
import io.milvus.param.dml.InsertParam;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
public class MilvusTest {

    @Test
    public void connect() {
        @Cleanup
        MilvusServiceClient milvusClient = new MilvusServiceClient(
                ConnectParam.newBuilder()
                        .withHost("127.0.0.1")
                        .withPort(19530)
                        .withDatabaseName("test")
                        .build()
        );
        //DB 생성
//        milvusClient.dropDatabase(DropDatabaseParam.newBuilder().withDatabaseName("test").build());
//        milvusClient.createDatabase(CreateDatabaseParam.newBuilder().withDatabaseName("test").build());

        //스키마 생성
        FieldType fieldType1 = FieldType.newBuilder()
                .withName("test_id")
                .withDataType(DataType.Int64)
                .withPrimaryKey(true)
                .withAutoID(false)
                .build();
        FieldType fieldType2 = FieldType.newBuilder()
                .withName("word_count")
                .withDataType(DataType.Int64)
                .build();
        FieldType fieldType3 = FieldType.newBuilder()
                .withName("test_intro")
                .withDataType(DataType.FloatVector)
                .withDimension(2)
                .build();
        CreateCollectionParam createCollectionReq = CreateCollectionParam.newBuilder()
                .withCollectionName("test_table")
                .withDescription("Test search")
                .withShardsNum(2)
                .addFieldType(fieldType1)
                .addFieldType(fieldType2)
                .addFieldType(fieldType3)
                .withEnableDynamicField(true)
                .build();
        //컬렉션(테이블) 생성
        milvusClient.createCollection(createCollectionReq);

        System.out.println(milvusClient.listDatabases().toString());
        System.out.println(milvusClient.showCollections(ShowCollectionsParam.newBuilder().withDatabaseName("test").build()));


        //데이터 삽입
//        Random ran = new Random();
//        List<Long> test_id_array = new ArrayList<>();
//        List<Long> word_count_array = new ArrayList<>();
//        List<List<Float>> test_intro_array = new ArrayList<>();
//        for (long i = 0L; i < 2000; ++i) {
//            test_id_array.add(i);
//            word_count_array.add(i + 10000);
//            List<Float> vector = new ArrayList<>();
//            for (int k = 0; k < 2; ++k) {
//                vector.add(ran.nextFloat());
//            }
//            test_intro_array.add(vector);
//        }
//        List<InsertParam.Field> fields = new ArrayList<>();
//        fields.add(new InsertParam.Field("test_id", test_id_array));
//        fields.add(new InsertParam.Field("word_count", word_count_array));
//        fields.add(new InsertParam.Field("test_intro", test_intro_array));
//
//        InsertParam insertParam = InsertParam.newBuilder()
//                .withCollectionName("test_table")
//                .withPartitionName("test1")
//                .withFields(fields)
//                .build();
//        milvusClient.insert(insertParam);

    }


    @Test
    public void connectCloudDB() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://in03-48ea815ee435730.api.gcp-us-west1.zillizcloud.com/v1/vector/collections"))
                .header("Authorization", "Bearer 13c89a6e07f885c63d6569cd9f43431750a13ffd9db6cd27d0791f3c91ec8e3557e169af6b8497f83b54efb883156f0d3df3333f")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.body());
    }
}
