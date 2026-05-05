package org.example.demo_httpsession.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @GetMapping("/login")
    public ResponseEntity<String> login(HttpSession session){
        session.setAttribute("userName", "admin");
        return ResponseEntity.ok("login success");
    }

    @GetMapping("/profile")
    public ResponseEntity<String> profile(HttpSession session){
        if(session.getAttribute("userName") != null){
            return ResponseEntity.ok("Xin chào : " +  session.getAttribute("userName"));
        }
        return new ResponseEntity<>("Vui lòng đăng nhập", HttpStatus.UNAUTHORIZED);
    }
}
