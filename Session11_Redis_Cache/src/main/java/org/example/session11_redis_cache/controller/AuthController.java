package org.example.session11_redis_cache.controller;



import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class AuthController {
    @GetMapping("/login")
    public ResponseEntity<String> login(HttpSession session) {
        // Ép kiểu tường minh và thử với một chuỗi đơn giản không dấu
        session.setAttribute("employee", "Admin_Rikkei");
        return new ResponseEntity<>("Đăng nhập thành công!", HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<String> profile(HttpSession session) {
        if (session.getAttribute("employee") != null) {
            String name = (String) session.getAttribute("employee");
            name = "Xin chào : " + name ;
            return new ResponseEntity<>(name, HttpStatus.OK);
        }
        return new ResponseEntity<>("Vui lòng đăng nhập trước nhé ",HttpStatus.OK);
    }
}
