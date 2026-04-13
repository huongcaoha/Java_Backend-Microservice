package com.example.customerservice.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRequestDTO {
    @NotNull
    private long customerId ;
    @NotNull
    private long productId ;
    @NotNull
    @Min(0)
    private int quantity;
}
