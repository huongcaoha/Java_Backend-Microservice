package org.example.pharmacykafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic stockTopic() {
        return TopicBuilder.name("medicine-stock-events")
                .partitions(3)
                .build();
    }

    @Bean
    public NewTopic priceTopic() {
        return TopicBuilder.name("medicine-price-updates")
                .partitions(1)
                .build();
    }

    @Bean
    public NewTopic notifyTopic() {
        return TopicBuilder.name("pharmacy-notifications")
                .partitions(2)
                .build();
    }
}
