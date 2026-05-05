package org.example.session11_redis_cache.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.session11_redis_cache.model.dto.TransportAlertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransportProducer {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void alertToTopicTransport(TransportAlertDTO transportAlertDTO) throws JsonProcessingException {
        String stringObject = objectMapper.writeValueAsString(transportAlertDTO);
        stringRedisTemplate.convertAndSend("transport-alerts",stringObject);
    }
}
