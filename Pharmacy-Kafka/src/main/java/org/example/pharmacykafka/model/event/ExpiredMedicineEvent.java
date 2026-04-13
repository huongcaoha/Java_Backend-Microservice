package org.example.pharmacykafka.model.event;

import jakarta.validation.constraints.Pattern;
import lombok.*;


import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor     // Tạo Constructor đầy đủ tham số
@NoArgsConstructor      // Tạo Constructor không tham số (Bắt buộc phải có để Jackson Deserializer hoạt động)
@Builder                // Hỗ trợ tạo object theo phong cách Builder pattern
public class ExpiredMedicineEvent implements Serializable {

    private Long medicineId;      // ID của thuốc để Consumer dễ dàng tìm trong DB
    private LocalDate expiryDate;    // Ngày hết hạn (có thể dùng String hoặc LocalDate)
    private Integer currentStock; // Số lượng tồn kho hiện tại sắp hết hạn
    private String message;       // Nội dung cảnh báo chi tiết
}
