package Alumni.backend.module.exception;

public class NoExistsException extends RuntimeException{//학교 이메일이 아닌경우 발생하는 exception
    public NoExistsException(String message) {
        super(message);
    }
}