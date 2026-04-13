package org.example.pharmacykafka.service;

import org.example.pharmacykafka.model.entity.Medicine;
import org.example.pharmacykafka.model.event.ExpiredMedicineEvent;
import org.example.pharmacykafka.model.event.ExpiredMedicineListEvent;
import org.example.pharmacykafka.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockScannerService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private MedicineRepository medicineRepository;

    // Mô phỏng: Cứ mỗi 1p quét kho một lần
    @Scheduled(fixedRate = 60000  )
    public void scanExpiringMedicines() {
        List<Medicine> medicineListExpired = medicineRepository.getMedicinesExpired(LocalDate.now().plusDays(-30));
        if(!medicineListExpired.isEmpty()){
            List<ExpiredMedicineEvent> expiredMedicineEvents = new ArrayList<>();
            for (Medicine medicine : medicineListExpired) {
                expiredMedicineEvents.add(new ExpiredMedicineEvent(medicine.getId(),medicine.getExpiryDate(),medicine.getQuantity(),"Thuốc còn 30 ngày nữa là hết hạn"));
            }
            ExpiredMedicineListEvent expiredMedicineListEvent = new ExpiredMedicineListEvent();
            expiredMedicineListEvent.setExpiredMedicines(expiredMedicineEvents);
            expiredMedicineListEvent.setBatchId(Long.toString(System.currentTimeMillis()));
            expiredMedicineListEvent.setScanTimestamp(LocalDateTime.now());
            // Gửi sự kiện vào Kafka
            kafkaTemplate.send("pharmacy-notifications",expiredMedicineListEvent.getBatchId() , expiredMedicineListEvent);
            System.out.println(">>> Đã gửi cảnh báo hết hạn cho "+medicineListExpired.size()+" loại thuốc");
        }


    }
}
