//package org.example.demo_httpsession.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
//import org.springframework.session.web.http.CookieHttpSessionIdResolver;
//import org.springframework.session.web.http.DefaultCookieSerializer;
//import org.springframework.session.web.http.HttpSessionIdResolver;
//
//@Configuration
//@EnableRedisHttpSession // Kích hoạt Spring Session Redis
//public class RedisSessionConfig {
//
//    // BƯỚC 1: Ép dữ liệu lưu xuống Redis dưới dạng JSON (thay vì Binary)
//    @Bean
//    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
//        return RedisSerializer.json();
//    }
//
//    // BƯỚC 2: Cấu hình Cookie - "Người đưa thư"
//    @Bean
//    public HttpSessionIdResolver httpSessionIdResolver() {
//        CookieHttpSessionIdResolver resolver = new CookieHttpSessionIdResolver();
//        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
//
//        cookieSerializer.setCookieName("SESSION"); // Tên viên kẹo Cookie
//        cookieSerializer.setCookiePath("/");       // Có hiệu lực toàn trang
//        cookieSerializer.setUseBase64Encoding(false); // Tắt mã hóa Base64 để dễ Debug
//
//        resolver.setCookieSerializer(cookieSerializer);
//        return resolver;
//    }
//}
