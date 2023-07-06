package Alumni.backend.infra.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResponse extends BasicResponse {

    private int code;
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
