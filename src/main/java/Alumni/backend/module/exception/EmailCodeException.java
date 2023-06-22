package Alumni.backend.module.exception;

public class EmailCodeException extends IllegalArgumentException{
    public EmailCodeException(String message) {
        super(message);
    }
}
