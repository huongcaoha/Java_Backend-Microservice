package com.example.doctor_service.repository;

import com.example.doctor_service.model.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    @Query("from Doctor d where d.doctorName like concat('%',:name,'%') ")
    List<Doctor> searchDoctorByName(@Param("name") String name);
}
