package Alumni.backend.infra.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "게시글 검색 성공 형식 Response")
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
