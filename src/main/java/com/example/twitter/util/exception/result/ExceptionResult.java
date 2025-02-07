package com.example.twitter.util.exception.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionResult {
    private String type;
    private String message;

   public ExceptionResult(String message){
       this.message = message;
       this.type = "UnknownError";
   }

}
