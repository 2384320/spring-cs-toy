package com.project.cs.common.response;

import com.project.cs.common.response.code.ErrorCode;
import com.project.cs.common.response.code.SuccessCode;
import com.project.cs.common.exception.ValidResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class CommonResponse {

    private int code;
    private String message;
    private Object result;

    public static CommonResponse success(SuccessCode successCode, Object result) {
        return CommonResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .result(result)
                .build();
    }

    public static CommonResponse success(SuccessCode successCode) {
        return CommonResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build();
    }

    public static CommonResponse failure(ErrorCode errorCode, BindingResult result) {
        return CommonResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .result(
                        result.getFieldErrors().stream()
                                .map(ValidResponse::new)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public static CommonResponse failure(ErrorCode errorCode) {
        return CommonResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }
}
