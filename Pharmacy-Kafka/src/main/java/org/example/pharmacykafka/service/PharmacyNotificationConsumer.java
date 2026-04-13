package org.example.pharmacykafka.service;

import jakarta.transaction.Transactional;
import org.example.pharmacykafka.model.entity.Medicine;
import org.example.pharmacykafka.model.event.ExpiredMedicineEvent;
import org.example.pharmacykafka.model.event.ExpiredMedicineListEvent;
import org.example.pharmacykafka.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PharmacyNotificationConsumer {

    @Autowired
    private MedicineRepository medicineRepository;

    @KafkaListener(topics = "pharmacy-notifications", groupId = "pharmacy-group-notifications")
    @Transactional
    public void handleExpiryNotification(ExpiredMedicineListEvent event) {
        System.out.println("<<< Nhận được cảnh báo thuốc hết hạn !");
        List<Medicine> medicineList = new ArrayList();
        for (ExpiredMedicineEvent ex : event.getExpiredMedicines()){

        }

        // Cập nhật trạng thái trong Database
        medicineRepository.findById(event.getMedicineId()).ifPresent(medicine -> {
            medicine.setStatus("CẦN NHẬP HÀNG"); // Trạng thái thực tế
            medicineRepository.save(medicine);
            System.out.println("=== Đã cập nhật trạng thái thuốc ID " + event.getMedicineId() + " thành CẦN NHẬP HÀNG");
        });
    }
}
