package com.example.customerservice.controller;

import com.example.customerservice.model.dto.request.OrderRequestDTO;
import com.example.customerservice.model.dto.response.Product;
import com.example.customerservice.model.entity.Order;
import com.example.customerservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO){
        return orderService.save(orderRequestDTO);
    }

    @GetMapping("/getProduct/{id}")
    public ResponseEntity<?> getProduct(@PathVariable String id){
        return orderService.getProductFromProductService2(id);
    }
}
