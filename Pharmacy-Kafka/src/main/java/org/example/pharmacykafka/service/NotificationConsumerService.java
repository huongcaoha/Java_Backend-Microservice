package org.example.pharmacykafka.service;

import org.example.pharmacykafka.model.event.OrderEvent;
import org.example.pharmacykafka.model.entity.Medicine;
import org.example.pharmacykafka.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RefreshScope
public class NotificationConsumerService {
    @Autowired
    private MailService mailService ;
    @Autowired
    private MedicineRepository medicineRepository;

    @KafkaListener(topics = "medicine-stock-events",groupId = "pharmacy-group2")
    public void handleSendNotification(OrderEvent event)
    {
        Medicine medicine = medicineRepository.findById(event.getMedicineId()).orElse(null);
       if (medicine != null) {
           String message = """
        Cảm ơn bạn đã đặt hàng tại Pharma Medicine:
        Tên thuốc : %s
        Số lượng  : %d
        Tổng tiền : %f
        """.formatted(medicine.getMedicineName(), event.getQuantity(),(event.getQuantity() * medicine.getPrice()));
           mailService.sendMail("huongcaoha1995@gmail.com","Xác nhận đơn hàng",message);
           System.out.println("Đã gửi thông báo tới email khách hàng");
       }
    }
}
