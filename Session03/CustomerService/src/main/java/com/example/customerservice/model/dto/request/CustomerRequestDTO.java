package com.example.customerservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerRequestDTO {
    @NotBlank

    private String fullName;
    @NotBlank

    private String email;
    @NotBlank

    private String password;
}
