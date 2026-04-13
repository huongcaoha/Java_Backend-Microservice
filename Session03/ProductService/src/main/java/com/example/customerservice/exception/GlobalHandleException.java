package com.example.customerservice.exception;

import com.example.customerservice.model.dto.response.ApiResponseError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandleException {
    @ExceptionHandler(CustomException.class)
    public ApiResponseError<String> handleCustomException(CustomException e) {
        ApiResponseError<String>  apiResponseError = new ApiResponseError<String>();
        apiResponseError.setMessage(e.getMessage());
        apiResponseError.setError("CustomException");
        apiResponseError.setStatus(404);
        apiResponseError.setTimestamp(LocalDateTime.now());
        return apiResponseError;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponseError<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        ApiResponseError<Map<String, String>>  apiResponseError = new ApiResponseError<>();
        apiResponseError.setMessage(errorMap);
        apiResponseError.setError("MethodArgumentNotValidException");
        apiResponseError.setStatus(400);
        apiResponseError.setTimestamp(LocalDateTime.now());
        return apiResponseError;
    }


}
