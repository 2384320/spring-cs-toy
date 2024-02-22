package com.project.cs.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SVCException extends RuntimeException {
    private ErrorCode errorCode;
}