package com.example.customerservice.securty;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JWTFilterChain extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Đọc thông tin đã được Gateway giải mã JWT và forward qua Header
        String username = request.getHeader("X-User-Username");
        String role = request.getHeader("X-User-Role"); // Ví dụ: "ROLE_USER" hoặc "ROLE_ADMIN"

        if (username != null && role != null) {
            // Tạo danh sách quyền từ Header role nhận được
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

            // Tạo đối tượng chứng thực cho Spring Security
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // ĐẶC BIỆT QUAN TRỌNG: Đăng ký vào Context để pass qua check .authenticated()
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // Nếu không có thông tin xác thực từ Gateway chuyển qua, trả về thẳng 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Missing User Information from Gateway");
            return;
        }

        filterChain.doFilter(request, response);
    }
}