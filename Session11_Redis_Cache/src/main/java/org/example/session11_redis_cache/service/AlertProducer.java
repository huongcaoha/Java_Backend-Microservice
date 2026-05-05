package org.example.session11_redis_cache.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.session11_redis_cache.model.dto.AlertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class AlertProducer {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendAlert(AlertDTO alertDTO) throws JsonProcessingException {
        String stringMapper = objectMapper.writeValueAsString(alertDTO);
        // Gửi tin nhắn vào kênh "pharmacy-alerts"
        redisTemplate.convertAndSend(alertDTO.getTopicListen(), stringMapper);
    }
}
