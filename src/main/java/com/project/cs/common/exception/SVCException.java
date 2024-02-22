package com.project.cs.common.exception;

import com.project.cs.common.response.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SVCException extends RuntimeException {
    private ErrorCode errorCode;
}