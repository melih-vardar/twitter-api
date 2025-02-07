package com.example.twitter.util.exception.type;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BusinessException extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }

}
