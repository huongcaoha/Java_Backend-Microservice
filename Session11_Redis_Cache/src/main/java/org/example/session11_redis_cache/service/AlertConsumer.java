package org.example.session11_redis_cache.service;

import org.example.session11_redis_cache.model.dto.AlertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class AlertConsumer {
    @Autowired
    private ObjectMapper objectMapper;
    // Hàm này phải trùng tên với khai báo trong listenerAdapter
    public void handleAlert(String stringObject) {
        AlertDTO alertDTO = objectMapper.readValue(stringObject, AlertDTO.class);
        System.out.println("=== THÔNG BÁO DASHBOARD QUẢN LÝ ===");
        System.out.println("Type : " + alertDTO.getType());
        System.out.println("Nội dung: " + alertDTO.getMessage());
        System.out.println("===================================");
    }
}
