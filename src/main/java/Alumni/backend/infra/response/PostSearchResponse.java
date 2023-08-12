package Alumni.backend.infra.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "게시글 검색 성공 형식 Response")
public class PostSearchResponse<T> extends BasicResponse {

    @Schema(description = "응답 코드", defaultValue = "200")
    private int code; // 응답 코드
    @Schema(description = "응답 메시지")
    private String message; // 응답 메시지
    @Schema(description = "데이터의 개수")
    private int count; // 데이터의 개수
    @Schema(description = "hashTag 순위", example = "[\"python\", \"Java\"]")
    private List<String> tagRank; // hashTag 순위
    @Schema(description = "데이터 결과")
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
