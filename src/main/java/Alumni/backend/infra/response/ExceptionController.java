package Alumni.backend.infra.response;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice // 모든 Controller 전역에서 발생할 수 있는 예외를 잡아 처리해주는 어노테이션 + ResponseBody
public class ExceptionController {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<? extends BasicResponse> IllegalArgumentHandler(
      IllegalArgumentException e) {
    log.warn("------------IllegalArgumentException-------------");
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<? extends BasicResponse> RunTimeHandler(RuntimeException e) {
    log.warn("------------RunTimeException-------------");
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<? extends BasicResponse> methodValidException(
      MethodArgumentNotValidException e) {
    log.warn("------------MethodArgumentNotValidException-------------");
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
}
