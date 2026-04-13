package com.example.customerservice.model.dto.response;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Product {
    private Long id ;
    private String name;
    private double price;
    private int stockQuantity;
}
