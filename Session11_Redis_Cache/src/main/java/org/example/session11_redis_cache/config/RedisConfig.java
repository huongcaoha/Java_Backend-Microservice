package org.example.session11_redis_cache.config;

import org.example.session11_redis_cache.service.AlertConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    // 1. Bộ chuyển đổi: Chỉ định class và hàm sẽ xử lý tin nhắn khi nhận được
    @Bean
    MessageListenerAdapter listenerAdapter(AlertConsumer consumer) {
        // chỉ định class AlertConsumer sẽ dùng method handleAlert xử lý khi nhận đc tin nhắn
        return new MessageListenerAdapter(consumer, "handleAlert");
    }

    // 2. Container lắng nghe: Đăng ký "kênh" pharmacy-alerts
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // Đăng ký nghe kênh "pharmacy-alerts"
        container.addMessageListener(listenerAdapter, new PatternTopic("pharmacy-alerts"));
        return container;
    }
}
