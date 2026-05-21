package org.example.session13_jwt_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Lấy SecretKey từ cấu hình ứng dụng (ví dụ: application.yml)
    @Value("${application.jwt.secret}")
    private String SECRET_KEY;

    // Lấy SecretKey dạng Base64 và decode thành SecretKey object
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decode Base64 string to byte array
        return Keys.hmacShaKeyFor(keyBytes);                 // Tạo SecretKey từ byte array
    }

    // Trích xuất tất cả các claims từ JWT
    public Claims extractAllClaims(String token) {
        return Jwts.parser()                                // Bắt đầu xây dựng parser cho JWT
                .verifyWith(getSigningKey())                    // Đặt SecretKey để xác thực chữ ký
                .build()                                           // Xây dựng parser
                .parseSignedClaims(token)                             // Phân tích JWT và trả về Jws<Claims>
                .getPayload();                                        // Lấy phần body (payload) chứa các claims
    }

    // Trích xuất một claim cụ thể từ JWT
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Trích xuất tất cả claims
        return claimsResolver.apply(claims);             // Áp dụng hàm riêng để lấy claim mong muốn
    }

    // Trích xuất username từ JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Subject thường chứa username/ID người dùng
    }

    // Kiểm tra xem token đã hết hạn chưa
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date()); // Lấy thời gian hết hạn và so sánh với thời gian hiện tại
    }

    // Kiểm tra tính hợp lệ của token
    public boolean validateToken(String token) {
        try {
            // Chỉ cần gọi extractAllClaims, nếu có lỗi (chữ ký không hợp lệ, token hết hạn...) sẽ ném ngoại lệ
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return !isTokenExpired(token); // Kiểm tra thêm thời gian hết hạn (dù Jwts.parserBuilder cũng làm điều này)
        } catch (Exception e) {
            // Log lỗi: SignatureException, ExpiredJwtException, MalformedJwtException...
            System.err.println("JWT Validation Error: " + e.getMessage());
            return false; // Token không hợp lệ
        }
    }
}
