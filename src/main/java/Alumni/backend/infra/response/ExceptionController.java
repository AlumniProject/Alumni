package Alumni.backend.infra.response;

import Alumni.backend.infra.exception.*;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice // 모든 Controller 전역에서 발생할 수 있는 예외를 잡아 처리해주는 어노테이션 + ResponseBody
public class ExceptionController {

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<? extends BasicResponse> UnAuthorizedException(UnAuthorizedException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(ImageException.class)
    public ResponseEntity<? extends BasicResponse> ImageException(ImageException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(FormalValidationException.class)
    public ResponseEntity<? extends BasicResponse> formalValidationException(FormalValidationException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<? extends BasicResponse> RunTimeHandler(RuntimeException e) {

        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "RUNTIME_ERROR"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<? extends BasicResponse> methodValidException(
            MethodArgumentNotValidException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "VALID_ERROR"));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<? extends BasicResponse> usernameNotFoundException(
            UsernameNotFoundException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "USER_NOT_FOUND"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<? extends BasicResponse> exception(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "UNEXPECTED_ERROR"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<? extends BasicResponse> httpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "HTTP_REQUEST_ERROR"));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<? extends BasicResponse> tokenExpiredException(
            TokenExpiredException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "JWT_NOT_VALID"));
    }
}
