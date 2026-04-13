//package org.example.pharmacykafka.config;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.example.pharmacykafka.model.event.OrderEvent;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
//import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
//
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableKafka
//public class KafkaConsumerConfig {
//
//    @Bean
//    public ConsumerFactory<String, OrderEvent> consumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "pharmacy-group");
//
//        // Cấu hình Deserializer cho Key và Value
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//
//        // Cấu hình chi tiết cho JsonDeserializer bên trong ErrorHandlingDeserializer
//        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class);
//        props.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "*");
//        props.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, "org.example.pharmacykafka.model.event.OrderEvent");
//
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        // Bây giờ kiểu dữ liệu đã khớp hoàn toàn (String, OrderEvent)
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
//}