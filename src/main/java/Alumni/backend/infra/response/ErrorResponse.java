package Alumni.backend.infra.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
