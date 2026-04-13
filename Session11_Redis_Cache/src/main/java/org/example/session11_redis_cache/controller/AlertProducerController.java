package org.example.session11_redis_cache.controller;

import org.example.session11_redis_cache.model.dto.AlertDTO;
import org.example.session11_redis_cache.service.AlertProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertProducerController {
    @Autowired
    private AlertProducer alertProducer;

    @PostMapping
    public String produceAlert(@RequestBody AlertDTO alertDTO) {
        alertProducer.sendAlert(alertDTO);
        return "Đã gửi thông báo đến kênh : " + alertDTO.getTopicListen();
    }
}
