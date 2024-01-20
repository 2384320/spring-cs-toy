package com.project.cs.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ErrorCode[] errorCodes;

    @PostConstruct
    public void init() {
        errorCodes = ErrorCode.values();
    }

    @ExceptionHandler
    public ResponseEntity serverExceptionHandler(Exception e) {
        log.error("정의되지 않거나 서버 에러: {}", e.getMessage(), e);

        ResultData resultData;
        if (e instanceof HttpMessageNotReadableException)
            resultData = new ResultData(ErrorCode.SERVER_ERROR_JSON_PARSE);
        else
            resultData = new ResultData(ErrorCode.SERVER_ERROR_UNDEFINED);
        return new ResponseEntity<>(resultData, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity serviceExceptionHandler(SVCException e) {
        ResultData resultData;

        if (Arrays.stream(errorCodes).anyMatch(errorCode -> errorCode == e.getErrorCode()))
            resultData = new ResultData(e.getErrorCode());
        else {
            log.error("정의되지 않은 에러: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ResultData(ErrorCode.SERVER_ERROR_UNDEFINED), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity validExceptionHandler(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ResultData(ErrorCode.INPUT_ERROR_VALIDATION, e.getBindingResult()), HttpStatus.BAD_REQUEST);
    }
}