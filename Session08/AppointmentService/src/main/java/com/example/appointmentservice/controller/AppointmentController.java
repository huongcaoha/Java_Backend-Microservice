package com.example.appointmentservice.controller;

import com.example.appointmentservice.model.dto.request.AppointmentDTO;
import com.example.appointmentservice.model.dto.response.ApiResponseError;
import com.example.appointmentservice.model.entity.Appointment;
import com.example.appointmentservice.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ApiResponseError<String> addAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return appointmentService.AddAppointment(appointmentDTO);
    }
}
