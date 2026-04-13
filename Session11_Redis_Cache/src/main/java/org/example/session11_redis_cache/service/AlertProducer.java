package org.example.session11_redis_cache.service;

import org.example.session11_redis_cache.model.dto.AlertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class AlertProducer {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendAlert(AlertDTO alertDTO) {
        String stringMapper = objectMapper.writeValueAsString(alertDTO);
        // Gửi tin nhắn vào kênh "pharmacy-alerts"
        redisTemplate.convertAndSend(alertDTO.getTopicListen(), stringMapper);
    }
}
