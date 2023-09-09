package Alumni.backend.module.service.contest;

import Alumni.backend.module.domain.contest.Contest;
import Alumni.backend.module.repository.contest.ContestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingService {
    private final ContestRepository contestRepository;

    public void contestCrawling() throws IOException, InterruptedException {

        String url = "https://www.wevity.com/?c=find&s=1&gub=1&cidx=20";//위비티 url

        //시스템 property 설정
        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/chromedriver-win32/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.addArguments("--lang=ko");//언어 설정
        options.addArguments("--no-sandbox");//sandbox 모드 비활성화
        options.addArguments("--disable-dev-shm-usage");//dev/shm 메모리 사용을 비홯성화
        options.addArguments("--disable-gpu");//GPU 가속 비활성화
        options.addArguments("--remote-allow-origins=*");//ConnectionFailedException 해결 방법
        options.setHeadless(true);
        options.setCapability("ignoreProtectedModeSettings", true);

        WebDriver webDriver = new ChromeDriver(options);

        webDriver.get(url);

        List<WebElement> concertElementList = webDriver.findElements(By.cssSelector("div .tit a"));//div 안 a태그에 있는거 가져오기
        List<String> urlLIst = new ArrayList<>();
        List<Contest> contestList = new ArrayList<>();

        for (WebElement concertEl : concertElementList){
            urlLIst.add(concertEl.getAttribute("href"));//각 공모전 상세 url 가져오기
        }

        for (String concertUrl : urlLIst) {
            //log.info(concertUrl);
            webDriver.get(concertUrl);

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10)); // 대기 시간 설정 (최대 10초)

            //공모전 image
            WebElement imgElement = webDriver.findElement(By.cssSelector(".thumb img"));
            String poster = imgElement.getAttribute("src");
//            log.info(poster);

            //분야
            WebElement fieldInfo = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cd-info-list li")));
            String[] field = fieldInfo.getText().split("\n");

            WebElement periodInfo = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".dday-area")));
            String period = periodInfo.getText().replaceAll("D-\\d+", "").trim();//디데이 부분 제거하기
            String[] cleanPeriod = period.split("\n");
            log.info(cleanPeriod[1]);

            //제목
            WebElement elementTitle = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".tit-area .tit")));
//            String title = elementTitle.getText();
//            log.info(title);
            //상세내용
            WebElement elementContent = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".article")));
//            String content = elementContent.getText();
//            log.info(content);

            Contest contest = Contest.builder()
                    .field(field[1])
                    .title(elementTitle.getText())
                    .content(elementContent.getText())
                    .period(cleanPeriod[1])
                    .link(concertUrl)
                    .poster(poster)
                    .likeNum(0)
                    .teamNum(0)
                    .build();

            contestList.add(contest);
            Thread.sleep(5000);
        }

        contestRepository.saveAll(contestList);

        webDriver.close();
        webDriver.quit();
    }
}
