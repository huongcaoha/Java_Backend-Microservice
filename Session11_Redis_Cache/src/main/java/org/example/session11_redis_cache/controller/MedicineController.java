package org.example.session11_redis_cache.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.session11_redis_cache.model.dto.TransportAlertDTO;
import org.example.session11_redis_cache.model.entity.Medicine;
import org.example.session11_redis_cache.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicine(@PathVariable Long id) {
        return new ResponseEntity<>(medicineService.getMedicineById(id), HttpStatus.OK);
    }

    @PutMapping("/sell/{id}")
    public void sellMedicine(@PathVariable Long id) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
               String rsBuy = medicineService.sellMedicine(id);
                System.out.println("Người dùng 1 : " + rsBuy);
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String rsBuy = medicineService.sellMedicine(id);
                System.out.println("Người dùng 2 : " + rsBuy);
            }
        });
        thread2.start();
        thread1.start();
    }

    @PutMapping("/warehouseExport/{id}")
    public void handleExportMedicine(@PathVariable Long id , @RequestBody TransportAlertDTO transportAlertDTO) throws JsonProcessingException {
        System.out.println("Kết quả nếu 3 user cùng mua hàng 1 lúc : \n");
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String rs = medicineService.warehouseExport(id,transportAlertDTO);
                    System.out.println("User 1 :");
                    System.out.println(rs);
                    System.out.println();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String rs = medicineService.warehouseExport(id,transportAlertDTO);
                    System.out.println("User 2 : ");
                    System.out.println(rs);
                    System.out.println();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String rs = medicineService.warehouseExport(id,transportAlertDTO);
                    System.out.println("User 3 : ");
                    System.out.println(rs);
                    System.out.println();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
