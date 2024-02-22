package com.project.cs.common.exception;

import com.project.cs.common.response.CommonResponse;
import com.project.cs.common.response.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> serverExceptionHandler(Exception e) {
        log.error("정의되지 않거나 서버 에러: {}", e.getMessage(), e);

        CommonResponse response;
        if (e instanceof HttpMessageNotReadableException)
            response = CommonResponse.failure(ErrorCode.SERVER_ERROR_JSON_PARSE);
        else
            response = CommonResponse.failure(ErrorCode.SERVER_ERROR_UNDEFINED);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Object> serviceExceptionHandler(SVCException e) {
        return new ResponseEntity<>(
                CommonResponse.failure(
                        e.getErrorCode()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<Object> validExceptionHandler(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                CommonResponse.failure(
                        ErrorCode.INPUT_ERROR_VALIDATION,
                        e.getBindingResult()
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}