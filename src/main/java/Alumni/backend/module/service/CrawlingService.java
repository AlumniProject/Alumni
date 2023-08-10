package Alumni.backend.module.service;

import Alumni.backend.module.domain.CrawlingInfo;
import Alumni.backend.module.repository.CrawlingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingService {
    private final CrawlingInfoRepository crawlingInfoRepository;
    private WebDriver webDriver;

    public List<CrawlingInfo> getCrawlingInfos() throws IOException, InterruptedException {
        List<CrawlingInfo> crawlingInfoList = new ArrayList<>();

        String url = "http://ticket.interpark.com/TPGoodsList.asp?Ca=Liv&SubCa=For&tid4=For";

        log.info("interpark 크롤링 시작");

        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/chromedriver-win32/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.addArguments("--lang=ko");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        options.setCapability("ignoreProtectedModeSettings", true);

        webDriver = new ChromeDriver(options);

        webDriver.get(url);

        Thread.sleep(1000);

        List<WebElement> concertElementList = webDriver.findElements(By.cssSelector("td .fw_bold a"));
        List<String> urlLIst = new ArrayList<>();

        for (WebElement concertEl : concertElementList){
            urlLIst.add(concertEl.getAttribute("href"));
        }

        Thread.sleep(10000);

        for (String concertUrl : urlLIst) {
            webDriver.get(concertUrl);

            Thread.sleep(15000);

            WebElement elementTitle = webDriver.findElement(By.cssSelector(".prdTitle"));
            WebElement elementPlace = webDriver.findElement(By.cssSelector(".infoBtn"));
            WebElement elementDate = webDriver.findElement(By.cssSelector(".infoText"));
            String elementTime = getCrawlingTime(webDriver);
            String elementSigner = getCrawlingSinger(webDriver);
            List<String> priceList = getMinMaxPrice(webDriver);

            CrawlingInfo crawlingInfo = CrawlingInfo.builder()
                    .title(elementTitle.getText())
                    .place(elementPlace.getText())
                    .date(elementDate.getText())
                    .singer(elementSigner)
                    .time(elementTime)
                    .maxPrice(priceList.get(0))
                    .minPrice(priceList.get(1))
                    .build();

            crawlingInfoRepository.save(crawlingInfo);
            crawlingInfoList.add(crawlingInfo);

        }

        webDriver.close();
        webDriver.quit();

        return crawlingInfoList;
    }

    private String getCrawlingTime(WebDriver webDriver){
        String crawlingTime = "";
        try{
            WebElement webElement =webDriver.findElement(By.cssSelector(".timeTableLabel span"));
            crawlingTime = webElement.getText();
        }
        catch (NoSuchElementException e){
            crawlingTime = "NOT OPENED";
        }

        return crawlingTime;
    }

    private String getCrawlingSinger(WebDriver webDriver){
        String crawlingSinger = "";

        try{
            WebElement webElement = webDriver.findElement(By.cssSelector(".castingName"));

            crawlingSinger = webElement.getText();
        }
        catch (NoSuchElementException e){
            crawlingSinger = "NULL";
        }

        return crawlingSinger;
    }

    private List<String> getMinMaxPrice(WebDriver webDriver){
        List<String> priceList = new ArrayList<>();

        try {
            List<WebElement> elementPriceList = webDriver.findElements(By.cssSelector(".infoPriceItem .price"));

            Optional<String> maxPriceOptional = elementPriceList.stream().map(WebElement::getText).findFirst();
            Optional<String> minPriceOptional= Optional.empty();

            if (elementPriceList.size()==0){
                minPriceOptional = Optional.of("0");
            }
            else {
                minPriceOptional = elementPriceList.stream().skip(elementPriceList.size() - 1).map(WebElement::getText).findFirst();
            }

            priceList.add(maxPriceOptional.orElse("0"));
            priceList.add(minPriceOptional.orElse("0"));
        }
        catch (NoSuchElementException e){
            priceList.add("0");
            priceList.add("0");
        }

        return priceList;

    }
}