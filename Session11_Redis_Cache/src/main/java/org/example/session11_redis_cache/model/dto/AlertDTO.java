package org.example.session11_redis_cache.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class AlertDTO {
    private String topicListen;
    private String message;
    private String type ;
}
