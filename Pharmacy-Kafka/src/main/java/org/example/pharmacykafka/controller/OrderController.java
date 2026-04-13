package org.example.pharmacykafka.controller;

import org.example.pharmacykafka.model.event.OrderEvent;
import org.example.pharmacykafka.service.PharmacyProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pharmacy")
public class OrderController {

    @Autowired
    private PharmacyProducerService producerService;

    @PostMapping("/sell")
    public String sellMedicine(@RequestParam Long medicineId, @RequestParam Integer quantity) {
        OrderEvent event = new OrderEvent(
                UUID.randomUUID().toString(),
                medicineId,
                quantity
        );

        producerService.sendOrderEvent(event);
        return "Thanh toán thành công! Sự kiện đã được gửi tới Kafka.";
    }
}
