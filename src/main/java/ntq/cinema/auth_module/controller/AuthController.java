package ntq.cinema.auth_module.controller;

import lombok.RequiredArgsConstructor;
import ntq.cinema.auth_module.dto.request.LoginRequest;
import ntq.cinema.auth_module.dto.request.RegisterRequest;
import ntq.cinema.auth_module.dto.request.VerifyOtpRequest;
import ntq.cinema.auth_module.dto.response.JwtResponse;
import ntq.cinema.auth_module.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("${api.ntq-cinema-url}/auth")
@RequestMapping("${api.ntq-cinema-url}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // ========== 1. TẠO TÀI KHOẢN ==========
    // 1.1. ĐĂNG KÝ TÀI KHOẢN BẰNG EMAIL
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request); // Gọi service đăng ký
            return ResponseEntity.ok("Đăng ký thành công! Vui lòng kiểm tra email để lấy mã OTP.");
        } catch (RuntimeException ex) {
            // Nếu có lỗi (ví dụ: email đã tồn tại), trả về HTTP 400 Bad Request
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            // Bắt các lỗi bất ngờ khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    // 1.2. XÁC THỰC OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        try {
            authService.verifyOtp(request); // Gọi service xác thực OTP
            return ResponseEntity.ok("Xác thực OTP thành công! Tài khoản đã được kích hoạt.");
        } catch (RuntimeException ex) {
            // Nếu có lỗi như: sai mã OTP, mã OTP đã hết hạn...
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            // Bắt các lỗi bất ngờ khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + ex.getMessage());
        }
    }


    // ĐĂNG NHẬP
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        JwtResponse tokens = authService.login(request);
        return ResponseEntity.ok(tokens);
    }

    // LÀM MỚI TOKEN
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(@RequestParam String refreshToken) {
        JwtResponse newTokens = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(newTokens);
    }

    // ĐĂNG XUẤT
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok("Đăng xuất thành công!");
    }
}
