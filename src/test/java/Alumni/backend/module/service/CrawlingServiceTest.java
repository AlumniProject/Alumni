package Alumni.backend.module.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@SpringBootTest
@Transactional
public class CrawlingServiceTest {
    @Autowired
    CrawlingService crawlingService;

    @Test
    @Rollback(value = false)
    void 공모전크롤링테스트() throws IOException, InterruptedException {
        crawlingService.contestCrawling();
    }
}
