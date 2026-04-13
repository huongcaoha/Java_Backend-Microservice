package com.example.appointmentservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Doctor {
    private long id;
    private String doctorName;
    private String specialty ;
}
