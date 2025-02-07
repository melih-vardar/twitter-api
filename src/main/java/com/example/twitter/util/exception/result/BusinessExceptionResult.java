package com.example.twitter.util.exception.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BusinessExceptionResult extends ExceptionResult{

    public BusinessExceptionResult(String message) {
        super(message);
        this.setType("BusinessException");
    }

}
