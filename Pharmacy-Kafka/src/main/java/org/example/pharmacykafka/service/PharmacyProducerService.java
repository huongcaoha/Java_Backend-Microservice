package org.example.pharmacykafka.service;

import org.example.pharmacykafka.model.event.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RefreshScope
public class PharmacyProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "medicine-stock-events";

    public void sendOrderEvent(OrderEvent event) {
        // Thử thách: Truyền medicineId làm Key (tham số thứ 2)
        // Việc này đảm bảo các đơn hàng cùng loại thuốc luôn vào cùng 1 Partition
        String key = String.valueOf(event.getMedicineId());

        kafkaTemplate.send(TOPIC, key, event);

        System.out.println("Đã gửi đơn hàng: " + event.getOrderId() + " với Key: " + key);
    }
}
