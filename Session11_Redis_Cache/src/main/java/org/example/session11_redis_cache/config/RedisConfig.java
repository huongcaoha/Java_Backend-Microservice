package org.example.session11_redis_cache.config;

import org.example.session11_redis_cache.service.AlertConsumer;
import org.example.session11_redis_cache.service.TransportConsumerAlert;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;

@Configuration

public class RedisConfig {

    // 1. Bộ chuyển đổi: Chỉ định class và hàm sẽ xử lý tin nhắn khi nhận được
    @Bean
    MessageListenerAdapter listenerAdapter(AlertConsumer consumer) {
        // chỉ định class AlertConsumer sẽ dùng method handleAlert xử lý khi nhận đc tin nhắn
        return new MessageListenerAdapter(consumer, "handleAlert");
    }

    @Bean
    MessageListenerAdapter listenerAdapterTransport(TransportConsumerAlert transportConsumerAlert) {
        // chỉ định class AlertConsumer sẽ dùng method handleAlert xử lý khi nhận đc tin nhắn
        return new MessageListenerAdapter(transportConsumerAlert, "handleAlertToTransport");
    }

    // 2. Container lắng nghe: Đăng ký "kênh" pharmacy-alerts
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter,
                                            MessageListenerAdapter listenerAdapterTransport) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // Đăng ký nghe kênh "pharmacy-alerts"
        container.addMessageListener(listenerAdapter, new PatternTopic("pharmacy-alerts"));
        container.addMessageListener(listenerAdapterTransport, new PatternTopic("transport-alerts"));
        return container;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // Cấu hình kết nối đến Redis đơn (Single Server)
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379");

        return Redisson.create(config);
    }
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return RedisSerializer.json(); // Lưu dữ liệu dạng JSON cho dễ soi lỗi
    }
}
