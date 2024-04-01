package com.ponyo.fastfood.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ponyo.fastfood.domain.PlaceDTO;
import com.ponyo.fastfood.domain.ReviewDTO;
import com.ponyo.fastfood.util.ExcelHelper;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component
public class DriverManager {
    private WebDriver driver;

    @PostConstruct
    private void init() {
//        WebDriverManager.chromedriver().clearDriverCache().setup();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @PreDestroy
    private void destroy() {
        driver.quit();
    }

    public void searchKeywordFromKakaoMap(String keyword){
        String url = "https://map.kakao.com";

        driver.get(url);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));
        log.info("driver = " + driver.getTitle());

        //검색
        WebElement search = driver.findElement(By.cssSelector("div.box_searchbar > input.query"));
        search.sendKeys(keyword);
        search.sendKeys(Keys.ENTER);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));

        //장소 버튼 클릭
        driver.findElement(By.xpath("//*[@id=\"info.main.options\"]/li[2]/a")).sendKeys(Keys.ENTER);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));
    }

    public HashMap<String, Object> crawl(String type){
        switch (type){
            case "KAKAO":
                return crawlFromKakao();
            case "GOOGLE":
                return crawlFromGoogle();
            case "NAVER":
                return crawlFromNaver();
            default:
                return crawlFromKakao();
        }
    }
    private HashMap<String, Object> crawlFromKakao(){
        HashMap<String, Object> result = new HashMap<>();
        try{
            result = getStoreList(1, result);
            WebElement btn = driver.findElement(By.xpath("//*[@id=\"info.search.place.more\"]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

            int page = 0;
//            for(int i=2;i<3;i++){
//                page++;
//                String xPath = "//*[@id=\"info.search.page.no" + page + "\"]";
//                driver.findElement(By.xpath(xPath)).sendKeys(Keys.ENTER);
//                Thread.sleep(1000);
//                result = getStoreList(i, result);
//
//                if(i % 5 == 0){
//                    driver.findElement(By.xpath( "//*[@id=\"info.search.page.next\"]")).sendKeys(Keys.ENTER);
//                    page = 0;
//                }
//            }
        }catch (InterruptedException e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }catch (NoSuchElementException e){
            log.error(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
        }

        log.info("**크롤링 완료**");
        return result;
    }

    private HashMap<String, Object> crawlFromGoogle(){
        HashMap<String, Object> result = new HashMap<>();
        return result;
    }

    private HashMap<String, Object> crawlFromNaver(){
        HashMap<String, Object> result = new HashMap<>();
        return result;
    }

    private HashMap<String, Object> getStoreList(int page, HashMap<String, Object> result) throws InterruptedException,NoSuchElementException, Exception{
        List<PlaceDTO> placeList = new ArrayList<>();
        List<ReviewDTO> list = new ArrayList<>();
        if(result.containsKey("placeList")){
            placeList = (List<PlaceDTO>) result.get("placeList");
        }
        if(result.containsKey("reviewList")){
            list = (List<ReviewDTO>) result.get("reviewList");
        }

        Thread.sleep(3);
        WebElement search = driver.findElement(By.cssSelector("div.box_searchbar > input.query"));
        List<WebElement> storeList = driver.findElements(By.cssSelector(".placelist > .PlaceItem"));

        for (int i=0;i<3;i++) {
            Thread.sleep(3);
            WebElement store = storeList.get(i);
            String placeId = "place_"+page+"_"+(i + 1);

            String name = store.findElement(By.cssSelector(".head_item > .tit_name > .link_name")).getText();
            String addr = store.findElement(By.cssSelector(".info_item > .addr")).getText();
            String tel = store.findElement(By.cssSelector(".info_item > .contact > .phone")).getText();
            String degree = store.findElement(By.cssSelector(".rating > .score > .num")).getText();
            degree = "".equals(degree) ? "0" : degree;
            String period = store.findElement(By.cssSelector(".info_item > .openhour > .periodWarp > a")).getText();
            log.info("{},{},{},{},{}", name, addr, tel, degree, period);

            if (Float.parseFloat(degree) >= 3.0) {
                //상세보기 (시설정보, 리뷰 키워드, 메뉴)
                List<String> menu = new ArrayList<String>();
                List<String> placeInfo = new ArrayList<String>();
                store.findElement(By.cssSelector(".info_item > .contact > a")).sendKeys(Keys.ENTER);
                Thread.sleep(200);
                ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                driver.switchTo().window(tabs.get(1));
                Thread.sleep(200);
                //시설 정보
                List<WebElement> places = driver.findElements(By.cssSelector(".list_facility > li"));
                for(WebElement el : places){
                    String info = el.findElement(By.cssSelector(".color_g")).getText();
                    try{
                        WebElement detail = el.findElement(By.cssSelector(".color_g > .screen_out"));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].style.position = 'static';", detail);
                        info += detail.getText();
                    }catch (NoSuchElementException e){}
                    placeInfo.add(info);
                }

                //메뉴
                WebElement menuList = driver.findElement(By.cssSelector(".list_menu"));
                try{
                    driver.findElement(By.cssSelector(".cont_menu > .link_more")).sendKeys(Keys.ENTER);
                }catch (NoSuchElementException e){}
                List<WebElement> menus = menuList.findElements(By.cssSelector(".loss_word"));
                List<WebElement> menusPrice = menuList.findElements(By.cssSelector(".price_menu"));
                for(int idx=0;idx<menus.size();idx++){
                    menu.add(menus.get(idx).getText() + "(" + menusPrice.get(idx).getText() + ")");
                }
                Thread.sleep(1000);

                //리뷰 수집
                //리뷰 (상위 5개만 수집)
                try{
                    driver.findElement(By.cssSelector(".evaluation_review > .link_more")).sendKeys(Keys.ENTER);
                }catch (NoSuchElementException e){}
                Thread.sleep(1000);
                List<WebElement> reviewList = driver.findElements(By.cssSelector(".list_evaluation > li"));
                int size = reviewList.size() > 5 ? 5: reviewList.size();
                for(int j=0; j < size;j++){
                    ReviewDTO review = new ReviewDTO();
                    WebElement el = reviewList.get(j);
                    String reviewTx = el.findElement(By.cssSelector(".txt_comment > span")).getText();
                    String rating = starRate(el.findElement(By.cssSelector(".inner_star")).getAttribute("style"));
                    String date = el.findElement(By.cssSelector("div > span.time_write")).getText();
                    String user = el.findElement(By.cssSelector(".inner_user > .txt_username")).getText();

                    review.setPlaceId(placeId);
                    review.setUser(user);
                    review.setReview(reviewTx);
                    review.setRating(rating);
                    review.setDate(date);
                    list.add(review);
                }

                driver.close();
                driver.switchTo().window(tabs.get(0));

                PlaceDTO place = new PlaceDTO();
                place.setPlaceId(placeId);
                place.setName(name);
                place.setAddr(addr);
                place.setTel(tel);
                place.setDegree(degree);
                place.setMenu(menu.toString());
                place.setPlaceInfo(placeInfo.toString());
                place.setPeriod(period);
                place.setLatlon(findGeoPoint(addr));
                placeList.add(place);
            }
        }
        result.put("placeList", placeList);
        result.put("reviewList", list);
        return result;
    }

    private void crawlFromKakaoAndExportExcel(){
        try{
            writeFileStoreList(1);
            WebElement btn = driver.findElement(By.xpath("//*[@id=\"info.search.place.more\"]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            int page = 0;
            for(int i=2;i<3;i++){
                page++;
                String xPath = "//*[@id=\"info.search.page.no" + page + "\"]";
                driver.findElement(By.xpath(xPath)).sendKeys(Keys.ENTER);
                Thread.sleep(1000);
                writeFileStoreList(i);

                if(i % 5 == 0){
                    driver.findElement(By.xpath( "//*[@id=\"info.search.page.next\"]")).sendKeys(Keys.ENTER);
                    page = 0;
                }
            }
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        log.info("**크롤링 완료**");
    }
    private void writeFileStoreList(int page) throws InterruptedException {
        //이름, 주소, 전화번호, 메뉴, 별점,리뷰 키워드, 영업정보
        Thread.sleep(200);
        WebElement search = driver.findElement(By.cssSelector("div.box_searchbar > input.query"));
        String fileNm = search.getAttribute("value").replace(" ", "_")+"_목록_";
        List<WebElement> storeList = driver.findElements(By.cssSelector(".placelist > .PlaceItem"));

        List<LinkedHashMap<String, String>> result = new ArrayList<>();
        List<LinkedHashMap<String,String>> list = new ArrayList<>();

        for (int i=0;i<storeList.size();i++) {
            Thread.sleep(200);
            WebElement store = storeList.get(i);
            String placeId = "place_"+page+"_"+(i + 1);
            LinkedHashMap<String, String> temp = new LinkedHashMap<>();

            String name = store.findElement(By.cssSelector(".head_item > .tit_name > .link_name")).getText();
            String addr = store.findElement(By.cssSelector(".info_item > .addr")).getText();
            String tel = store.findElement(By.cssSelector(".info_item > .contact > .phone")).getText();
            String degree = store.findElement(By.cssSelector(".rating > .score > .num")).getText();
            degree = "".equals(degree) ? "0" : degree;
            String period = store.findElement(By.cssSelector(".info_item > .openhour > .periodWarp > a")).getText();
            log.info("{},{},{},{},{}", name, addr, tel, degree, period);

            //상세보기 (시설정보, 리뷰 키워드, 메뉴)
            List<String> menu = new ArrayList<String>();
            List<String> placeInfo = new ArrayList<String>();
            store.findElement(By.cssSelector(".info_item > .contact > a")).sendKeys(Keys.ENTER);
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1));
            Thread.sleep(200);
            try{
                //시설 정보
                List<WebElement> places = driver.findElements(By.cssSelector(".list_facility > li"));
                for(WebElement el : places){
                    String info = el.findElement(By.cssSelector(".color_g")).getText();
                    try{
                        WebElement detail = el.findElement(By.cssSelector(".color_g > .screen_out"));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].style.position = 'static';", detail);
                        info += detail.getText();
                    }catch (NoSuchElementException e){}
                    placeInfo.add(info);
                }
                log.info("places info : {}", placeInfo);
                //메뉴
                WebElement menuList = driver.findElement(By.cssSelector(".list_menu"));
                driver.findElement(By.cssSelector(".link_more")).sendKeys(Keys.ENTER);
                List<WebElement> menus = menuList.findElements(By.cssSelector(".loss_word"));
                List<WebElement> menusPrice = menuList.findElements(By.cssSelector(".price_menu"));
                for(int idx=0;idx<menus.size();idx++){
                    log.info("menu : {}", menus.get(idx).getText());
                    log.info("price : {}", menusPrice.get(idx).getText());
                    menu.add(menus.get(idx).getText() + "(" + menusPrice.get(idx).getText() + ")");
                }
                log.info("menu info : {}", menu);

                //리뷰 수집
                //리뷰 (상위 10개만 수집)
                driver.findElement(By.xpath("//a[@class='link_more'][1]")).sendKeys(Keys.ENTER);
                Thread.sleep(3);
                List<WebElement> reviewList = driver.findElements(By.cssSelector(".list_evaluation > li"));
                for(int j=0; j < (reviewList.size() > 5 ? 5: reviewList.size());j++){
                    LinkedHashMap<String,String> review = new LinkedHashMap<String,String>();
                    WebElement el = reviewList.get(j);
                    String reviewTx = el.findElement(By.cssSelector(".txt_comment > span")).getText();
                    String rating = starRate(el.findElement(By.cssSelector(".inner_star")).getAttribute("style"));
                    String date = el.findElement(By.cssSelector("div > span.time_write")).getText();
                    String user = el.findElement(By.cssSelector(".inner_user > .txt_username")).getText();

                    review.put("placeId", placeId);
                    review.put("user", user);
                    review.put("review", reviewTx);
                    review.put("rating", rating);
                    review.put("date", date);
                    list.add(review);
                }

                driver.close();
                driver.switchTo().window(tabs.get(0));
            }catch (NoSuchElementException e){
                driver.close();
                driver.switchTo().window(tabs.get(0));
            }
            float review = Float.parseFloat(degree);
            if (review >= 3.0) {
                temp.put("id", placeId);
                temp.put("name", name);
                temp.put("addr", addr);
                temp.put("tel", tel);
                temp.put("degree", degree);
                temp.put("menu", menu.toString());
                temp.put("placeInfo", placeInfo.toString());
                temp.put("period", period);
                temp.put("latlon", findGeoPoint(addr));
                result.add(temp);
            }
        }
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        HashMap excelInfo = new HashMap();
        String[] headers = new String[]{"id","name", "addr","tel", "degree","menu","placeInfo","period","latlon"};
        excelInfo.put("sheet", fileNm + now +".xlsx");
        excelInfo.put("headers", headers);

        HashMap reviewExcelInfo = new HashMap();
        String[] reviewHeaders = new String[]{"placeId","user", "review","rating","date"};
        reviewExcelInfo.put("sheet", fileNm + "리뷰_" + now +".xlsx");
        reviewExcelInfo.put("headers", reviewHeaders);

        if(page == 1){
            ExcelHelper.crtExcel(result, excelInfo);
            ExcelHelper.crtExcel(list, reviewExcelInfo);
        }else{
            ExcelHelper.writeExcel(result, excelInfo);
            ExcelHelper.writeExcel(list, reviewExcelInfo);
        }
    }
    private String findGeoPoint(String location) {
        if(location.contains("\n")){
            String road = location.split("\n")[0];
            String parcel = location.split("\n")[1];
            location = road;
        }
        String apikey = "A602BC1C-25C5-35C3-BF29-C1D649AD88FB";
        String searchType = "road"; //PARCEL : 지번주소 ROAD : 도로명주소
        String epsg = "epsg:4326";
        String result = "";

        StringBuilder sb = new StringBuilder("https://api.vworld.kr/req/address");
        sb.append("?service=address");
        sb.append("&request=getCoord");
        sb.append("&format=json");
        sb.append("&crs=" + epsg);
        sb.append("&key=" + apikey);
        sb.append("&type=" + searchType);
        sb.append("&address=" + URLEncoder.encode(location, StandardCharsets.UTF_8));

        try{
            URL url = new URL(sb.toString());
            @Cleanup
            InputStreamReader stream  = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
            @Cleanup
            BufferedReader reader = new BufferedReader(stream);

            JSONParser jspa = new JSONParser();
            ObjectMapper objectMapper = new ObjectMapper();

            Object jsob = jspa.parse(reader);
            JSONObject data = new JSONObject();
            if(jsob instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) jsob;
                data = new JSONObject(map);

                JSONObject jsrs = data.getJSONObject("response");
                JSONObject jsResult = jsrs.getJSONObject("result");
                JSONObject jspoitn = jsResult.getJSONObject("point");
                result = jspoitn.get("y") + "," + jspoitn.get("x");
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private String starRate(String style){
        //style = width:10%
        double result = 0;
        String val =style.split(":")[1].trim();
        int num = Integer.parseInt(val.substring(0, val.indexOf("%")));
        result = num / 20;
        return String.valueOf(result);
    }
}
