package org.example.jwt_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class JwtGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service-route", r -> r
                        .path("/api/v1/products/**") // Khớp đường dẫn
                        .uri("lb://PRODUCTSERVICE")   // Đẩy sang Eureka service viết hoa
                )
                // Nếu sau này thầy có thêm order-service thì chỉ cần nối tiếp:
                // .route("order-service-route", r -> r.path("/api/v1/orders/**").uri("lb://ORDERSERVICE"))
                .build();
    }
}
