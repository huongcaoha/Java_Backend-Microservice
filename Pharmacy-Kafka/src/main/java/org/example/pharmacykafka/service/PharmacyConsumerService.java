package org.example.pharmacykafka.service;

import org.example.pharmacykafka.model.event.OrderEvent;
import org.example.pharmacykafka.model.entity.Medicine;
import org.example.pharmacykafka.model.entity.Order;
import org.example.pharmacykafka.repository.MedicineRepository;
import org.example.pharmacykafka.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RefreshScope
public class PharmacyConsumerService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MedicineRepository medicineRepository;

    @KafkaListener(topics = "medicine-stock-events", groupId = "pharmacy-group")
    public void consumeOrder(OrderEvent event) { // Spring sẽ tự hiểu và convert JSON sang OrderEvent
        System.out.println("Nhận đơn hàng mới: " + event.getOrderId());
        Medicine medicine = medicineRepository.findById(event.getMedicineId()).orElse(null);
        Order checkOrderExisted = orderRepository.findById(event.getOrderId()).orElse(null);
        if (medicine != null && checkOrderExisted == null && medicine.getQuantity() >= event.getQuantity()) {
            Order order = convertEventToOrder(event,medicine);
             orderRepository.save(order);
             medicine.setQuantity(medicine.getQuantity() - event.getQuantity());
             medicineRepository.save(medicine);
            System.out.println("Thêm mới đơn hàng thành công !");
        }else {
            System.out.println("Thêm mới đơn hàng thất bại");
        }
    }

    public Order convertEventToOrder(OrderEvent orderEvent,Medicine medicine) {
        Order newOrder = new Order();
        newOrder.setId(orderEvent.getOrderId());
        newOrder.setTimestamp(LocalDateTime.now());
        newOrder.setQuantity(orderEvent.getQuantity());
        newOrder.setPriceSell(medicine.getPrice());
        newOrder.setMedicine(medicine);
        newOrder.setTotalAmount(medicine.getPrice()*orderEvent.getQuantity());
        return newOrder;
    }
}