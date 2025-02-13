package com.example.twitter.util.exception;

import com.example.twitter.util.exception.result.BusinessExceptionResult;
import com.example.twitter.util.exception.result.ExceptionResult;
import com.example.twitter.util.exception.result.ValidationExceptionResult;
import com.example.twitter.util.exception.type.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({BusinessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BusinessExceptionResult handleBusinessException(BusinessException e){
        return new BusinessExceptionResult(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionResult handleValidationException(MethodArgumentNotValidException e){
        return new ValidationExceptionResult(
                e
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map((error) -> error.getDefaultMessage())
                .toList());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResult handleException(Exception e){
        return new ExceptionResult("An unexpected error occurred " + e.getMessage());
    }

}
