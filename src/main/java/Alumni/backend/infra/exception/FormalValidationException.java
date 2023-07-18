package Alumni.backend.infra.exception;

public class FormalValidationException extends IllegalArgumentException {

    public FormalValidationException(String message) {
        super(message);
    }
}
