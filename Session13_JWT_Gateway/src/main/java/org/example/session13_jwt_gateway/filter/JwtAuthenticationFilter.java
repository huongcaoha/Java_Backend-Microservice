package org.example.session13_jwt_gateway.filter;

import org.example.session13_jwt_gateway.security.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component // Đánh dấu là một Spring Component để Spring có thể quản lý
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtUtil jwtUtil; // Tiêm JwtUtil đã tạo ở trên
    private final RouterValidator routerValidator; // Service để kiểm tra nếu route không cần xác thực

    // Constructor để inject dependencies
    public JwtAuthenticationFilter(JwtUtil jwtUtil, RouterValidator routerValidator) {
        super(Config.class); // Gọi constructor của lớp cha
        this.jwtUtil = jwtUtil;
        this.routerValidator = routerValidator;
    }

    public static class Config {
        // Có thể thêm các cấu hình cho filter nếu cần, ví dụ: các route bỏ qua xác thực
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest(); // Lấy request hiện tại

            // 1. Kiểm tra xem request có yêu cầu xác thực JWT hay không
            // Các route như đăng nhập, đăng ký thường không cần xác thực
            if (routerValidator.isSecured.test(request)) { // Sử dụng Predicate để kiểm tra
                // 2. Kiểm tra xem header "Authorization" có tồn tại không
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    // Nếu không có header Authorization, trả về lỗi UNAUTHORIZED
                    return this.onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
                }

                // 3. Trích xuất Token từ header
                String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0); // Lấy giá trị đầu tiên
                if (authHeader != null && authHeader.startsWith("Bearer ")) { // Kiểm tra format Bearer token
                    String token = authHeader.substring(7); // Bỏ qua "Bearer " để lấy token
                    try {
                        // 4. Validate Token
                        if (!jwtUtil.validateToken(token)) {
                            // Nếu token không hợp lệ, trả về lỗi UNAUTHORIZED
                            return this.onError(exchange, "Invalid/Expired Token", HttpStatus.UNAUTHORIZED);
                        }
                        // 5. Nếu token hợp lệ, giải mã và chuyển tiếp thông tin người dùng
                        String username = jwtUtil.extractUsername(token); // Trích xuất username

                        // Thêm thông tin username vào header của request forward đến microservice
                        ServerHttpRequest mutatedRequest = request.mutate() // Tạo request mới từ request gốc
                                .header("X-Auth-Userid", username) // Thêm header tùy chỉnh
                                .build(); // Xây dựng request mới
                        return chain.filter(exchange.mutate().request(mutatedRequest).build()); // Chuyển tiếp request đã "biến đổi"

                    } catch (Exception e) {
                        // Bắt các ngoại lệ trong quá trình validate (MalformedJwtException, SignatureException, ExpiredJwtException...)
                        System.err.println("JWT Validation Error: " + e.getMessage());
                        return this.onError(exchange, "Auth Failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
                    }
                }
            }
            // Nếu không cần xác thực hoặc đã xử lý xong, chuyển tiếp request bình thường
            return chain.filter(exchange);
        };
    }

    // Hàm tiện ích để xử lý lỗi và trả về Response
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);             // Đặt mã trạng thái HTTP
        return exchange.getResponse().setComplete();                  // Hoàn thành response
    }
}
