package com.project.cs.exception;

import lombok.Data;
import org.springframework.validation.FieldError;


@Data
public class ValidResult {

    private String field;
    private String msg;
    private Object invalidValue;
    private String kind;

    public ValidResult(FieldError fieldError) {
        this.field = fieldError.getField();
        this.msg = fieldError.getDefaultMessage();
//        this.invalidValue = (fieldError.getRejectedValue() != null) ? fieldError.getRejectedValue() : "";
        this.invalidValue = fieldError.getRejectedValue();
        this.kind = fieldError.getCode();
    }

}