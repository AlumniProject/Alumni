package Alumni.backend.module.crawl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CrawlingServiceTest {

    @Autowired
    CrawlingService crawlingService;

    @Test
    @DisplayName("크롤링해서 정보 list top 정보 가져오기")
    public void 크롤링테스트() throws Exception {
        crawlingService.getContest();
    }
}