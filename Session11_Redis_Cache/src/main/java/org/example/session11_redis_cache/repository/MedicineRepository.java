package org.example.session11_redis_cache.repository;

import org.example.session11_redis_cache.model.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
}
