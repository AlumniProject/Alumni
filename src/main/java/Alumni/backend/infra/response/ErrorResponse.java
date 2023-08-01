package Alumni.backend.infra.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Error Response")
public class ErrorResponse extends BasicResponse {

    private int code;
    private String message;

    public ErrorResponse(String message) {
        this.code = 400;
        this.message = message;
    }

    public ErrorResponse(int errorCode, String message) {
        this.code = errorCode;
        this.message = message;
    }
}
