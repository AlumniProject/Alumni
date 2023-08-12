package Alumni.backend.infra.config;

import Alumni.backend.module.service.CrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CrawlingApplicationRunner implements ApplicationRunner {

    @Autowired
    private CrawlingService crawlingService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        crawlingService.contestCrawling();
        System.out.println("크롤링 완료");
    }
}