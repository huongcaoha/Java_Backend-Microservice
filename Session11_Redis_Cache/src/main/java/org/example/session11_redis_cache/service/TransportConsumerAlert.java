package org.example.session11_redis_cache.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.session11_redis_cache.model.dto.TransportAlertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransportConsumerAlert {
    @Autowired
    private ObjectMapper objectMapper;
    public void handleAlertToTransport(String stringObject) throws JsonProcessingException {
        TransportAlertDTO transportAlertDTO = objectMapper.readValue(stringObject, TransportAlertDTO.class);

        System.out.println("""
                Thông Báo Chuẩn Bị Vận Chuyển Hàng
           --------------------------------------
           Thời gian          : %s     
           Số lượng xe tải    : %d
           Giao đến điểm nhận : %s 
           --------------------------------------
        """.formatted(
                transportAlertDTO.getExportTime().toString(),   // Tương ứng với %s đầu tiên
                transportAlertDTO.getNumberTrucks(),     // Tương ứng với %d
                transportAlertDTO.getDestination()    // Tương ứng với %s cuối cùng
        ));
    }
}
