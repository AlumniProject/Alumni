package Alumni.backend.infra.exception;

public class EmailCodeException extends IllegalArgumentException {
    public EmailCodeException(String message) {
        super(message);
    }
}
