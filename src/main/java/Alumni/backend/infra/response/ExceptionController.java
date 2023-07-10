package Alumni.backend.infra.response;

import java.util.Objects;

import Alumni.backend.infra.exception.DuplicateNicknameException;
import Alumni.backend.infra.exception.EmailCodeException;
import Alumni.backend.infra.exception.NoExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice // 모든 Controller 전역에서 발생할 수 있는 예외를 잡아 처리해주는 어노테이션 + ResponseBody
public class ExceptionController {

    @ExceptionHandler(NoExistsException.class)
    public ResponseEntity<? extends BasicResponse> emailExHandle(NoExistsException e) {
        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(EmailCodeException.class)
    public ResponseEntity<? extends BasicResponse> tokenExHandle(EmailCodeException e) {
        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<? extends BasicResponse> maxSizeExHandle(MaxUploadSizeExceededException e) {
        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE.value(), "이미지 용량이 큽니다."));
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<? extends BasicResponse> DuplicateExHandle(DuplicateNicknameException e) {
        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<? extends BasicResponse> IllegalArgumentHandler(
            IllegalArgumentException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<? extends BasicResponse> RunTimeHandler(RuntimeException e) {

        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<? extends BasicResponse> methodValidException(
            MethodArgumentNotValidException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<? extends BasicResponse> usernameNotFoundException(
            UsernameNotFoundException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<? extends BasicResponse> exception(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }
}
