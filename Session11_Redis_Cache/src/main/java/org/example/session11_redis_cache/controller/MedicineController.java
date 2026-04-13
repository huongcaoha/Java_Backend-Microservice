package org.example.session11_redis_cache.controller;

import org.example.session11_redis_cache.model.entity.Medicine;
import org.example.session11_redis_cache.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicine(@PathVariable Long id) {
        return new ResponseEntity<>(medicineService.getMedicineById(id), HttpStatus.OK);
    }
}
