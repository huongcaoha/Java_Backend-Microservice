package com.example.customerservice.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponseError<T> {
    private LocalDateTime timestamp;
    private int status ;
    private T message;
    private String error ;
}
