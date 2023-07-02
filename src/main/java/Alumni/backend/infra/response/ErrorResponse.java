package Alumni.backend.infra.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends BasicResponse {

    private int code;
    private String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.code = 400;
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(int errorCode, String errorMessage) {
        this.code = errorCode;
        this.errorMessage = errorMessage;
    }
}
