package Alumni.backend.module.crawl;

import Alumni.backend.infra.exception.NoExistsException;
import io.netty.util.internal.ObjectUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

public class CrawlingUtils {

    public static Connection getJsoupConnection(String url) {
        try {
            return Jsoup.connect(url);
        } catch (Exception e) {
            throw new NoExistsException("JSOUP_CONN_ERROR");
        }
    }

    public static Document getJsoupDocuments(Connection connection, String url) {
        Connection conn = !ObjectUtils.isEmpty(connection) ? connection : getJsoupConnection(url);
        try {
            return conn.get();
        } catch (IOException e) {
            // connection 에러
            throw new NoExistsException("JSOUP_DOCUMENT_ERROR");
        }
    }

    public static Elements getJsoupElements(Connection connection,String url, String query) {
        Connection conn = !ObjectUtils.isEmpty(connection) ? connection : getJsoupConnection(url);
        try {
            return conn.get().select(query);
        } catch (IOException e) {
            // connection 에러
            throw new NoExistsException("JSOUP_ELEMENT_ERROR");
        }
    }
}
