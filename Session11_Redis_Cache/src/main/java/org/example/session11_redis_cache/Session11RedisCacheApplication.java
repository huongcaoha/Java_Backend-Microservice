package org.example.session11_redis_cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.SaveMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableCaching
@EnableRedisHttpSession(redisNamespace = "rikkei:pharmacy:session")
public class Session11RedisCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(Session11RedisCacheApplication.class, args);
    }

}
