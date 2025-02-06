package com.hyeji.petbook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/* 전역적으로 발생하는 예외를 처리하는 클래스 */
/* MethodArgumentNotValidException: @Valid에 의한 유효성 검사 실패 시 발생 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("모든 항목을 입력하세요.");

        BindingResult bindingResult = ex.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append("\n").append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }
}
