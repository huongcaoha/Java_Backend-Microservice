package org.example.session13_jwt_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    // Danh sách các endpoints không yêu cầu xác thực
    public static final List<String> openApiEndpoints = List.of(
            "/auth/register", // Ví dụ: Endpoint đăng ký
            "/auth/login",    // Ví dụ: Endpoint đăng nhập
            "/eureka"         // Ví dụ: Eureka Discovery Server (nếu dùng)
            // Thêm các public endpoints khác ở đây
    );

    // Predicate để kiểm tra xem một request có được bảo vệ (cần xác thực) hay không
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
