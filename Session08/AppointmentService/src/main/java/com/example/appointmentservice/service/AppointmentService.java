package com.example.appointmentservice.service;

import com.example.appointmentservice.model.dto.request.AppointmentDTO;
import com.example.appointmentservice.model.dto.response.ApiResponseError;
import com.example.appointmentservice.model.entity.Appointment;
import com.example.appointmentservice.model.entity.Doctor;
import com.example.appointmentservice.repository.AppointmentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private RestTemplate restTemplate ;

    @CircuitBreaker(name = "doctorServiceCB",fallbackMethod = "handleErrorAddAppointment")
    public ApiResponseError<String> AddAppointment(AppointmentDTO appointmentDTO) {
        String url = "http://doctor-service/api/v1/doctors/" +appointmentDTO.getDoctorId() ;
        Doctor doctor = restTemplate.getForObject(url, Doctor.class);
        if (doctor == null) {
            return ApiResponseError
                    .<String>builder()
                    .message("Doctor Not Found")
                    .error("Doctor Service Error")
                    .status(404)
                    .timestamp(LocalDateTime.now())
                    .build();
        }
        Appointment appointment = new Appointment();
        appointment.setAppointmentTime(LocalDateTime.now());
        appointment.setDoctorId(appointmentDTO.getDoctorId());
        appointment.setReason(appointmentDTO.getReason());
        appointment.setPatientName(appointmentDTO.getPatientName());
        appointmentRepository.save(appointment);
        return ApiResponseError
                .<String>builder()
                .message("Add Appointment Success")
                .error("Success")
                .status(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public ApiResponseError<String> handleErrorAddAppointment(AppointmentDTO appointmentDTO, Throwable t) {
        // Bạn nên log lỗi t.getMessage() ở đây để biết lỗi thực tế là timeout hay connection refused

        return ApiResponseError
                .<String>builder()
                .message("Hiện tại không thể kiểm tra thông tin bác sĩ, vui lòng thử lại sau vài giây")
                .error("Doctor Service Error")
                .status(503)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
