package org.example.pharmacykafka.repository;

import org.example.pharmacykafka.model.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long>{

    @Query("select m from Medicine m where m.expiryDate >= :date")
    List<Medicine> getMedicinesExpired(@Param("date") LocalDate date);
}
