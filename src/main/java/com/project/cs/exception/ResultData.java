package com.project.cs.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultData {
    int code;
    String msg;
    Object result;

    public ResultData(ErrorCode errorCode, BindingResult bindingResult) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
        this.result = bindingResult
                .getFieldErrors()
                .stream()
                .map(ValidResult::new).collect(Collectors.toList());
    }

    public ResultData(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }
}