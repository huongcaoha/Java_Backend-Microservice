package com.example.customerservice.model.dto.response;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Customer {
    private Long id;
    private String fullName;
    private String email;
    private String password;
}
