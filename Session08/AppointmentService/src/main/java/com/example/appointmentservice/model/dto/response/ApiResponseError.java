package com.example.appointmentservice.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApiResponseError<T> {
    private T message;
    private int status ;
    private String error;
    private LocalDateTime timestamp;
}
