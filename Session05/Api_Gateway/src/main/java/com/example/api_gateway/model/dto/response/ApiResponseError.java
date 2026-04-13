package com.example.api_gateway.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApiResponseError {
    private LocalDateTime timestamp;
    private String message;
    private String error;
    private int status ;
}
