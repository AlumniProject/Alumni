package Alumni.backend.infra.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "성공(Single) 형식 Response")
public class SingleResponse extends BasicResponse {

    @Schema(description = "응답 코드", defaultValue = "200")
    private int code;
    @Schema(description = "응답 메시지")
    private String message;

    public SingleResponse(String message) {
        this.code = 200;
        this.message = message;
    }

    public SingleResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
