package Alumni.backend.infra.exception;

public class UnAuthorizedException extends IllegalArgumentException { // 401 에러

    public UnAuthorizedException(String message) {
        super(message);
    }
}
