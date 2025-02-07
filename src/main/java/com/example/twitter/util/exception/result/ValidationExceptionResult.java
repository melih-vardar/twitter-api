package com.example.twitter.util.exception.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ValidationExceptionResult {
    private String type;
    private List<String> errors;

    public ValidationExceptionResult(List<String> errors) {
        this.errors = errors;
        this.type = "ValidationError";
    }
}
