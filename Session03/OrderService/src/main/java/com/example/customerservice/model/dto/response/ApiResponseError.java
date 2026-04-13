package com.example.customerservice.model.dto.response;

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
    private int status ;
    private String message;
    private String error ;
}
