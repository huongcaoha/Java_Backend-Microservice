package org.example.session11_redis_cache.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Tắt CSRF để có thể gọi POST từ Postman dễ dàng
                .csrf(csrf -> csrf.disable())

                // 2. Cấu hình quyền truy cập
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // 3. QUAN TRỌNG: Cấu hình quản lý Session
                .sessionManagement(session -> session
                        // Ép Spring Security sử dụng Session đã tồn tại (từ Redis)
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        return http.build();
    }
}
