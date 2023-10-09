package Alumni.backend.infra.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DataResponse<T> extends BasicResponse{
    @Schema(description = "응답 코드", defaultValue = "200")
    private int code; // 응답 코드
    @Schema(description = "응답 메시지")
    private String message; // 응답 메시지
    private T data;

    public DataResponse(T data, String message) {
        this.code = 200;
        this.message = message;
        this.data = data;
    }
}
