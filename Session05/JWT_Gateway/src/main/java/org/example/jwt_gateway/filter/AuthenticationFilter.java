package org.example.jwt_gateway.filter;

import io.jsonwebtoken.Claims;
import org.example.jwt_gateway.security.JWTProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    @Autowired
    private JWTProvider jwtProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // Các API không cần check token
    private final List<String> openEndpoints = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/public/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerWebExchange resetExchange = exchange.mutate().request(builder -> builder.headers(
                httpHeaders -> {
                    httpHeaders.remove("X-User-Username");
                    httpHeaders.remove("X-User-Role");
                }).build()
        ).build();
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 1. Kiểm tra xem URL hiện tại có nằm trong danh sách White-list hay không
        boolean isSecured = openEndpoints.stream().noneMatch(uri -> pathMatcher.match(uri, path));

        if (!isSecured) {
            return chain.filter(exchange); // Cho phép đi tiếp luôn
        }

        // 2. Kiểm tra Header Authorization
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Unauthorized: Missing token", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        // 3. Xác thực Token bằng thư viện jjwt
        if (!jwtProvider.validateToken(token)) {
            return onError(exchange, "Unauthorized: Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }

        // 4. Lấy thông tin claims và mutate request truyền xuống microservice phía sau
        Claims claims = jwtProvider.getClaimsFromToken(token);
        String username = claims.getSubject(); // Thường dùng subject lưu Id/Username
        String roles = claims.get("roles", String.class); // Giả sử bạn lưu claim tên là 'role'

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(builder -> builder
                        .header("X-User-Username", username)
                        .header("X-User-Role", roles)
                        .build())
                .build();

        return chain.filter(mutatedExchange);
    }

    // Hàm trả về lỗi Custom dạng Reactive
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        // Đặt giá trị ưu tiên cao (chạy rất sớm). Thường là -1 để xử lý xác thực trước 
        // khi các filter định tuyến hoặc load-balancer làm việc.
        return -1;
    }
}