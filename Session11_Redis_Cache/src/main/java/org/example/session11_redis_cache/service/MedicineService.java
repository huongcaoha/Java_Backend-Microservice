package org.example.session11_redis_cache.service;


import org.example.session11_redis_cache.model.entity.Medicine;
import org.example.session11_redis_cache.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

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
}
