package Alumni.backend.module.exception.advice;

import Alumni.backend.module.controller.MemberController;
import Alumni.backend.module.exception.EmailCodeException;
import Alumni.backend.module.exception.ErrorResult;
import Alumni.backend.module.exception.NoExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = MemberController.class)
public class MemberControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult emailExHandle(NoExistsException e){
        return new ErrorResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult tokenExHandle(EmailCodeException e){
        return new ErrorResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult illegalExHandle(IllegalArgumentException e){
        return new ErrorResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult serverExHandle(Exception e){
        return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}