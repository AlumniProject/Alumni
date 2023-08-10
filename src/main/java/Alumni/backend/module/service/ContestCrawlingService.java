package Alumni.backend.module.service;

import Alumni.backend.module.domain.CrawlingInfo;
import Alumni.backend.module.repository.CrawlingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContestCrawlingService {
    private final CrawlingInfoRepository crawlingInfoRepository;
    private WebDriver webDriver;

    public List<CrawlingInfo> getCrawlingInfos() throws IOException, InterruptedException {
        List<CrawlingInfo> crawlingInfoList = new ArrayList<>();

        String url = "https://www.wevity.com/";//festa url

        log.info("요즘것들 크롤링 시작");

        //시스템 property 설정
        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/chromedriver-win32/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.addArguments("--lang=ko");//언어 설정
        options.addArguments("--no-sandbox");//sandbox 모드 비활성화
        options.addArguments("--disable-dev-shm-usage");//dev/shm 메모리 사용을 비홯성화
        options.addArguments("--disable-gpu");//GPU 가속 비활성화
        options.addArguments("--remote-allow-origins=*");//ConnectionFailedException 해결 방법
        options.setCapability("ignoreProtectedModeSettings", true);

        webDriver = new ChromeDriver(options);

        webDriver.get(url);

        Thread.sleep(1000);

        List<WebElement> concertElementList = webDriver.findElements(By.cssSelector("div .tit a"));//div 안 a태그에 있는거 가져오기
        List<String> urlLIst = new ArrayList<>();

        for (WebElement concertEl : concertElementList){
            urlLIst.add(concertEl.getAttribute("href"));//각 공모전 상세 url 가져오기
        }

        Thread.sleep(10000);

        for (String concertUrl : urlLIst) {
            log.info(concertUrl);
            webDriver.get(concertUrl);

            Thread.sleep(15000);
        }

        webDriver.close();
        webDriver.quit();

        return crawlingInfoList;
    }
}