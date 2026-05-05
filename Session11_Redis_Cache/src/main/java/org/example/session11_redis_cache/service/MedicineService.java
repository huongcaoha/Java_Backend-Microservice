package org.example.session11_redis_cache.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.session11_redis_cache.model.dto.TransportAlertDTO;
import org.example.session11_redis_cache.model.entity.Medicine;
import org.example.session11_redis_cache.repository.MedicineRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MedicineService {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private TransportProducer transportProducer ;

    @Autowired
    private MedicineRepository medicineRepository;

    public String sellMedicine(Long medicineId) {
        // 1. Tạo chiếc chìa khóa định danh cho loại thuốc này
        RLock lock = redissonClient.getLock("lock:medicine:" + medicineId);

        try {
            // 2. Thử chiếm khóa: Đợi 3s, nếu lấy được thì giữ trong 5s
            boolean isLocked = lock.tryLock(3, 5, TimeUnit.SECONDS);

            if (isLocked) {
                // --- VÙNG AN TOÀN (CHỈ 1 NGƯỜI ĐƯỢC VÀO) ---
                Medicine medicine = medicineRepository.findById(medicineId).orElse(null);

                if (medicine != null && medicine.getQuantity() > 0) {
                    // Trừ kho
                    medicine.setQuantity(medicine.getQuantity() - 1);
                    medicineRepository.save(medicine);
                    return "Thanh toán thành công thuốc: " + medicine.getMedicineName();
                } else {
                    return "Sản phẩm đã hết hàng!";
                }
                // ------------------------------------------
            } else {
                return "Sản phẩm đang được xử lý bởi người khác, vui lòng đợi!";
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Lỗi hệ thống!";
        } finally {
            // 3. Luôn luôn nhả khóa để người sau vào (nhưng phải là chủ sở hữu khóa mới được nhả)
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    // Khi gọi hàm này, Spring sẽ check Redis trước với key là id của thuốc
    @Cacheable(value = "medicines", key = "#id")
    public Medicine getMedicineById(Long id) {
        // Giả lập hệ thống xử lý chậm để sinh viên thấy sự khác biệt
        try { Thread.sleep(2000); } catch (InterruptedException e) {}

        System.out.println("Đang truy vấn Database cho thuốc ID: " + id);
        return medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc!"));
    }

    @CacheEvict(value = "medicines",key = "#medicine.id")
    public Medicine updateMedicine(Medicine medicine){

        return medicineRepository.save(medicine);
    }

    @CacheEvict(value = "medicines",key="#medicineId")
    public String warehouseExport(long medicineId , TransportAlertDTO transportAlertDTO) throws JsonProcessingException {
        RLock rLock = redissonClient.getLock("lock:medicine:" + medicineId);
        if (rLock.tryLock()) {
            Medicine medicine = getMedicineById(medicineId);
            if (medicine == null && medicine.getQuantity() < transportAlertDTO.getQuantityStock()){
                return "Sản phẩm không đủ số lượng để xuất hoặc thuốc không tồn tại";
            }
            medicine.setQuantity(medicine.getQuantity() - transportAlertDTO.getQuantityStock());
            medicineRepository.save(medicine);
            transportProducer.alertToTopicTransport(transportAlertDTO);
            return "Đã hoàn thành xuất kho và thông báo đến đội vận chuyển";
        }else {
            return "Sản phẩm đang được cập nhật , vui lòng thử lại sau ";
        }

    }
}
