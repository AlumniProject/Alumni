package Alumni.backend.module.crawl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CrawlingService {

    @Value("${crawl.path}")
    private String url;

    public void getContest() {
        Document document = CrawlingUtils.getJsoupDocuments(null, url);
        Elements elements = document.getElementsByAttributeValue("class", "tit");

        List<String> titles = new ArrayList<>();
        List<String> subTitles = new ArrayList<>();

        for (int i = 0; i < elements.size(); i++) {
            if (i == 0)
                continue;
            Element element = elements.get(i);
            String title = element.select("a").text();
            String subTitle = element.select("div.sub-tit").text();

            titles.add(title);
            subTitles.add(subTitle);
        }
        log.info(titles.toString());
        log.info(subTitles.toString());
    }
}
