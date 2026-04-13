package com.example.customerservice.exception;

import com.example.customerservice.model.dto.response.ApiResponseError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalHandleException {
    @ExceptionHandler(CustomException.class)
    public ApiResponseError handleCustomException(CustomException e) {
        ApiResponseError  apiResponseError = new ApiResponseError();
        apiResponseError.setMessage(e.getMessage());
        apiResponseError.setError("CustomException");
        apiResponseError.setStatus(404);
        apiResponseError.setTimestamp(LocalDateTime.now());
        return apiResponseError;
    }


}
