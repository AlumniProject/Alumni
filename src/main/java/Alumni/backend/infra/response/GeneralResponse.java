package Alumni.backend.infra.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "성공(List) 형식 Response")
public class GeneralResponse<T> extends BasicResponse {

    private int code; // 응답 코드
    private String message; // 응답 메시지
    private int count; // 데이터의 개수
    private T data;

    public GeneralResponse(T data, String message) {
        this.data = data;
        this.message = message;
        this.code = 200;
        if (data instanceof List) {
            this.count = ((List<?>) data).size();
        } else {
            this.count = 1;
        }
    }
}
