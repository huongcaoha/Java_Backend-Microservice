package org.example.pharmacykafka.model.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEvent {
    private String orderId;
    private Long medicineId;
    private Integer quantity;

}
