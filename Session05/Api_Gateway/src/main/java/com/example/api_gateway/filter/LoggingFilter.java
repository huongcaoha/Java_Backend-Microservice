package com.example.api_gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getURI().getPath();
//        System.out.println("Incoming request to:" + path);
//        return chain.filter(exchange);
//    }

    @Override
    public Mono filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        org.springframework.http.server.reactive.ServerHttpRequest request = exchange.getRequest();

// 1. Ưu tiên lấy IP thật từ Header do Nginx hoặc Load Balancer truyền vào
        String ipAddress = request.getHeaders().getFirst("X-Forwarded-For");

// 2. Nếu Header trên trống, thử với X-Real-IP
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeaders().getFirst("X-Real-IP");
        }

// 3. Nếu vẫn không có (tức là gọi trực tiếp không qua Proxy), dùng cách mặc định
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            java.net.InetSocketAddress remoteAddress = request.getRemoteAddress();
            if (remoteAddress != null && remoteAddress.getAddress() != null) {
                ipAddress = remoteAddress.getAddress().getHostAddress();
            } else {
                ipAddress = "Không xác định";
            }
        } else {
            // Lưu ý: X-Forwarded-For có thể trả về một chuỗi nhiều IP (Client IP, Proxy 1, Proxy 2...)
            // IP gốc của Client luôn nằm ở vị trí đầu tiên
            ipAddress = ipAddress.split(",")[0].trim();
        }

        String path = request.getPath().toString();
        String method = request.getMethod().toString();

        System.out.println(">>> [Gateway Log] Client IP: " + ipAddress + " | Method: " + method + " | Path: " + path);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
