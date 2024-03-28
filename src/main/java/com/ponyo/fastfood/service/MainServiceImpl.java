package com.ponyo.fastfood.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final DriverManager driver;
    @Override
    public HashMap<String, Object> crawlFromKakao() {
//        String[] keywords = {"양재동 식당", "양재동 카페"};
        String keyword = "양재동 식당";
        try {
            driver.searchKeywordFromKakaoMap(keyword);
            Thread.sleep(1000);
            HashMap<String, Object> data = driver.crawl("KAKAO");
            return data;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
