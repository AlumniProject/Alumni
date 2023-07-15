package Alumni.backend.infra.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostSearchResponse<T> extends BasicResponse {

    private int code; // 응답 코드
    private String message; // 응답 메시지
    private int count; // 데이터의 개수
    private List<String> tagRank; // hagtag 순위
    private T data;

    public PostSearchResponse(T data, List<String> tagRank, String message) {
        this.data = data;
        this.message = message;
        this.tagRank = tagRank;
        this.code = 200;
        if (data instanceof List) {
            this.count = ((List<?>) data).size();
        } else {
            this.count = 1;
        }
    }
}
