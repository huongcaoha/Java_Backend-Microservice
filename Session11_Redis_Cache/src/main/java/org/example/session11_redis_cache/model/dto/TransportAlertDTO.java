package org.example.session11_redis_cache.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransportAlertDTO {
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exportTime;
    private int numberTrucks;
    private String destination;
    private int quantityStock ;
}
