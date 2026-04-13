package com.example.customerservice.service;

import com.example.customerservice.exception.CustomException;
import com.example.customerservice.model.dto.request.CustomerLoginDTO;
import com.example.customerservice.model.dto.request.CustomerRequestDTO;
import com.example.customerservice.model.dto.response.ApiResponseError;
import com.example.customerservice.model.dto.response.CustomerResponse;
import com.example.customerservice.model.entity.Customer;
import com.example.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository  customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomerResponse createCustomer(CustomerRequestDTO customerRequestDTO){
        Customer customer = Customer
                .builder()
                .fullName(customerRequestDTO.getFullName())
                .email(customerRequestDTO.getEmail())
                .password(passwordEncoder.encode(customerRequestDTO.getPassword()))
                .build();
        Customer newCustomer = customerRepository.save(customer);
        return CustomerResponse
                .builder()
                .id(newCustomer.getId())
                .fullName(newCustomer.getFullName())
                .email(newCustomer.getEmail())
                .build();
    }

    public ResponseEntity<CustomerResponse> getCustomerById(Long id){
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer == null){
            throw new CustomException("Khách hàng với id " + id + " không tồn tại !");
        }
        CustomerResponse customerResponse = CustomerResponse
                .builder()
                .email(customer.getEmail())
                .fullName(customer.getFullName())
                .id(customer.getId())
                .build();
        return ResponseEntity.ok(customerResponse);
    }

    public ResponseEntity<?> login(CustomerLoginDTO customerLoginDTO){
        try {
            Customer customer = customerRepository.findByEmail(customerLoginDTO.getEmail()).orElse(null);
            if(customer == null){
                return new ResponseEntity<>("username or password incorrect", HttpStatus.BAD_REQUEST);
            }
            if(!passwordEncoder.matches(customer.getPassword(),customerLoginDTO.getPassword())){
                return new ResponseEntity<>("username or password incorrect", HttpStatus.BAD_REQUEST);
            }else {
                CustomerResponse customerResponse = CustomerResponse
                        .builder()
                        .email(customer.getEmail())
                        .fullName(customer.getFullName())
                        .id(customer.getId())
                        .build();
                return ResponseEntity.ok(customerResponse);
            }
        }catch (Exception e){
            ApiResponseError apiResponseError = ApiResponseError
                    .builder()
                    .status(503)
                    .timestamp(LocalDateTime.now())
                    .error("Service Unavailable")
                    .message("Hệ thống quản lý bác sĩ hiện không khả dụng. Vui lòng đặt lịch sau!")
                    .build();
            return new ResponseEntity<>(apiResponseError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
