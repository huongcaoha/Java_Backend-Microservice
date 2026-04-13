package com.example.doctor_service.controller;

import com.example.doctor_service.model.dto.response.ApiResponse;
import com.example.doctor_service.model.entity.Doctor;
import com.example.doctor_service.repository.DoctorRepository;
import com.example.doctor_service.service.DoctorService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/{id}")
    public Doctor findDoctorById(@PathVariable long id){
        return doctorService.findById(id);
    }

    @GetMapping("/search")
    public ApiResponse<?> searchDoctorByName(@RequestParam("name") String name){
        return doctorService.searchDoctorByName(name);
    }

    @GetMapping("/insurance/{id}")
    public CompletableFuture<ApiResponse<String>> findInsuranceById(@PathVariable long id){
        return doctorService.findInsuranceById(id);
    }
}
