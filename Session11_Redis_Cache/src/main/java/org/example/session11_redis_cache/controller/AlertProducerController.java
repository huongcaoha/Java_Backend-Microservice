package org.example.session11_redis_cache.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.session11_redis_cache.model.dto.AlertDTO;
import org.example.session11_redis_cache.model.dto.TransportAlertDTO;
import org.example.session11_redis_cache.service.AlertProducer;
import org.example.session11_redis_cache.service.TransportProducer;
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
    @Autowired
    private TransportProducer transportProducer;

    @PostMapping
    public String produceAlert(@RequestBody AlertDTO alertDTO) throws JsonProcessingException {
        alertProducer.sendAlert(alertDTO);
        return "Đã gửi thông báo đến kênh : " + alertDTO.getTopicListen();
    }

    @PostMapping("/toTransport")
    public String produceTransport(@RequestBody TransportAlertDTO transportAlertDTO) throws JsonProcessingException {
        transportProducer.alertToTopicTransport(transportAlertDTO);
        return "Đã gửi thông báo đến bộ phận vận chuyển";
    }
}
