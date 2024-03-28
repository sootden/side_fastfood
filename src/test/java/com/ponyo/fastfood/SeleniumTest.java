package com.ponyo.fastfood;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class SeleniumTest {
    WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().clearDriverCache().setup();
    }

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }


    @Test
    public void crawlFromNaver() {
        driver.get("https://map.naver.com/p/search/대선칼국수");
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));

        System.out.println("driver = " + driver.getTitle());
        driver.switchTo().frame(driver.findElement(By.cssSelector("iframe#searchIframe")));


        List<WebElement> elements = driver.findElements(By.cssSelector(".C6RjW.place_bluelink"));

        System.out.println("TestTest**********************************");
        System.out.println("elements.size() = " + elements.size());
        elements.get(0).click();

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.switchTo().defaultContent();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
        driver.switchTo().frame(driver.findElement(By.cssSelector("iframe#entryIframe")));


        List<WebElement> placeSectionContents = driver.findElements(By.cssSelector(".place_section_content"));
        WebElement menuElement = placeSectionContents.get(placeSectionContents.size() - 1);
        List<WebElement> menus = menuElement.findElements(By.cssSelector("ul>li"));

        System.out.println("menumenu*******************************************");
        System.out.println("menus.size() = " + menus.size());

        for (WebElement menu : menus) {
            System.out.println(menu.getText());
        }
    }

    @Test
    public void crawlFromKakao(){
        String url = "https://map.kakao.com/";
        String keyword = "상도1동 카페";

        driver.get(url);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));
        System.out.println("driver = " + driver.getTitle());

        //검색
        WebElement search = driver.findElement(By.cssSelector("div.box_searchbar > input.query"));
        search.sendKeys(keyword);
        search.sendKeys(Keys.ENTER);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));

        //장소 버튼 클릭
        driver.findElement(By.xpath("//*[@id=\"info.main.options\"]/li[2]/a")).sendKeys(Keys.ENTER);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 크롤링 시작
        List<WebElement> storeList = driver.findElements(By.cssSelector(".placelist > .PlaceItem"));
        for (WebElement store : storeList) {
            System.out.println(store.findElement(By.cssSelector(".head_item")).getText());
        }

        System.out.println("**크롤링 완료**");
    }

    @Test
    public void crawlFromGoogle(){
        String keyword = "상도1동+카페";
        String url = "https://www.google.com/maps/search/"+keyword;

        driver.get(url);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));
        System.out.println("driver = " + driver.getTitle());

        //검색
//        waitInput(driver);

//        WebElement search = driver.findElement(By.cssSelector("#searchboxinput"));
//        search.sendKeys(keyword);
//        search.sendKeys(Keys.ENTER);
//        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 크롤링 시작
        try{
//          검색 결과로 나타나는 scroll-bar 포함한 div 잡고 스크롤 내리기
            WebElement scroll = driver.findElement(By.xpath("//*[@id=\"QA0Szd\"]/div/div/div[1]/div[2]/div/div[1]"));
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollBy(0,2000)", scroll);
//            Thread.sleep(1000);
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollBy(0,2000)", scroll);
//            Thread.sleep(1000);
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollBy(0,2000)", scroll);

//          한 칸 전체 데이터 가져오기
            List<WebElement> elements = driver.findElements(By.className("hfpxzc"));
            for(int i=0;i<elements.size();i++){
                System.out.println(elements.get(i).getAttribute("aria-label"));
            }
//            WebElement nextBtn = driver.findElement(By.cssSelector("#ppdPk-Ej1Yeb-LgbsSe-tJiF1e"));
//            nextBtn.click();
            Thread.sleep(2000);

        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

//        driver.findElement(By.className("sbcb_a")).click();

        System.out.println("**크롤링 완료**");
    }

    public static void waitInput(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(1000));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("searchboxinput")));
    }
}
