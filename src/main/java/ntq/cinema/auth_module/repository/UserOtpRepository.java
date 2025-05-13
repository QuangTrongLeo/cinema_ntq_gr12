package ntq.cinema.auth_module.repository;

import ntq.cinema.auth_module.entity.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {
    // 1.2.1. Tìm mã OTP trong DB dựa theo email và mã OTP từ thông tin của request
    UserOtp findByUserEmailAndOtpCode(String email, String otpCode);
}
