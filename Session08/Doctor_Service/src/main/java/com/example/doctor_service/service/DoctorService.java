package com.example.doctor_service.service;

import com.example.doctor_service.model.dto.response.ApiResponse;
import com.example.doctor_service.model.entity.Doctor;
import com.example.doctor_service.repository.DoctorRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public Doctor findById(long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    @RateLimiter(name = "searchDoctorLimit",fallbackMethod = "handleFallBackRateLimit")
    public ApiResponse<?> searchDoctorByName(String name) {
        return ApiResponse
                .<List<Doctor>>builder()
                .data(doctorRepository.searchDoctorByName(name))
                .message("Success")
                .status(200)
                .build();
    }

    public ApiResponse<?> handleFallBackRateLimit(String name , Throwable t){
        return ApiResponse
                .<String>builder()
                .data("Bạn đã gửi quá 5 request mỗi 10 giây")
                .message("Error")
                .status(429)
                .build();
    }

    @TimeLimiter(name = "insuranceTimeout" , fallbackMethod = "handleFallbackTimeout")
    public CompletableFuture<ApiResponse<String>> findInsuranceById(long id){
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ApiResponse
                    .<String>builder()
                    .status(200)
                    .message("Success")
                    .data("Lấy về thành công thông tin bảo hiểm có id là : " + id)
                    .build();
        });
    }

    public CompletableFuture<ApiResponse<String>> handleFallbackTimeout(long id,Throwable t){

        return CompletableFuture.completedFuture(ApiResponse
                .<String>builder()
                .status(200)
                .message("Dịch vụ bảo hiểm đang bận , vui lòng thanh toán trực tiếp.")
                .build());
    }

}
