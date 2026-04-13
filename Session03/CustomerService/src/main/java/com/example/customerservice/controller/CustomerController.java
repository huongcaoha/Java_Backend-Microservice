package com.example.customerservice.controller;

import com.example.customerservice.model.dto.request.CustomerLoginDTO;
import com.example.customerservice.model.dto.request.CustomerRequestDTO;
import com.example.customerservice.model.dto.response.CustomerResponse;
import com.example.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<CustomerResponse> register(@Valid @RequestBody CustomerRequestDTO customerRequestDTO){
        return new ResponseEntity<>(customerService.createCustomer(customerRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody CustomerLoginDTO customerLoginDTO){
        return customerService.login(customerLoginDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable Long id){
        return customerService.getCustomerById(id);
    }
}
