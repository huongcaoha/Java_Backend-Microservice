package com.example.appointmentservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentDTO {
    private String patientName;
    private long doctorId;
    private String reason; // Lý do khám bệnh
}
