package com.project.cs.common.exception;

import lombok.Data;
import org.springframework.validation.FieldError;

@Data
public class ValidResponse {

    private String field;
    private String message;
    private Object invalidValue;
    private String kind;

    public ValidResponse(FieldError fieldError) {
        this.field = fieldError.getField();
        this.message = fieldError.getDefaultMessage();
        this.invalidValue = fieldError.getRejectedValue();
        this.kind = fieldError.getCode();
    }
}