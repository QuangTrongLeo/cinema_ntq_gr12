package ntq.cinema.auth_module.service;

import ntq.cinema.auth_module.dto.request.LoginRequest;
import ntq.cinema.auth_module.dto.request.RegisterRequest;
import ntq.cinema.auth_module.dto.request.VerifyOtpRequest;
import ntq.cinema.auth_module.dto.response.JwtResponse;
import ntq.cinema.auth_module.entity.*;
import ntq.cinema.auth_module.enums.RoleTypeEnum;
import ntq.cinema.auth_module.mapper.JwtMapper;
import ntq.cinema.auth_module.repository.RefreshTokenRepository;
import ntq.cinema.auth_module.repository.RoleRepository;
import ntq.cinema.auth_module.repository.UserOtpRepository;
import ntq.cinema.auth_module.repository.UserRepository;
import ntq.cinema.auth_module.util.RandomOtpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
//@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserOtpRepository otpRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final JwtMapper jwtMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Value("${jwt.accessTokenExpiration}")
    private long accessTokenExpiration;
    @Value("${otp.expiry.minutes}")
    private long otpExpiryMinutes;

    public AuthService(UserRepository userRepository,
                       UserOtpRepository otpRepository,
                       RoleRepository roleRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       EmailService emailService,
                       JwtService jwtService, JwtMapper jwtMapper) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.jwtMapper = jwtMapper;
    }

    // ========== 1. TẠO TÀI KHOẢN ==========
    // ===== 1.1. ĐĂNG KÝ TÀI KHOẢN BẰNG EMAIL =====
    public void register(RegisterRequest request) {
        // 1.1.1. Kiểm tra User bằng email đã tồn tại trong DB chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            // Alternative Flows(1.1.1): Tài khoản bằng email đã tồn tại trong DB
            throw new RuntimeException("Tài khoản bằng email này đã tồn tại!");
        }

        // 1.1.2. Tìm Role với vai trò là CUSTOMER trong DB
        Role defaultCustomerRole = roleRepository.findByName(RoleTypeEnum.CUSTOMER)
                // Alternative Flows(1.1.2): Vai trò CUSTOMER không tồn tại trong DB
                .orElseThrow(() -> new RuntimeException("Vai trò CUSTOMER không tồn tại trong hệ thống"));

        // 1.1.3. Tạo User mới từ thông tin của request với trạng thái chưa kích hoạt
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setEnabled(false); // chưa kích hoạt

        // 1.1.4. Tạo UserRole và gán vai trò CUSTOMER cho user mới
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(defaultCustomerRole);

        // 1.1.5. Thêm UserRole vào danh sách vai trò của user
        user.getUserRoles().add(userRole);

        // 1.1.6. Lưu thông tin người dùng vào DB
        userRepository.save(user);

        // 1.1.7. Tạo mã OTP ngẫu nhiên và thiết lập thời gian hết hạn
        String otp = RandomOtpUtil.generateOtp();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiry = Timestamp.from(Instant.now().plus(otpExpiryMinutes, ChronoUnit.MINUTES)); // 5 phút
        UserOtp userOtp = new UserOtp(user, otp, expiry, now);

        // 1.1.8. Lưu mã Otp vào DB
        otpRepository.save(userOtp);

        // 1.1.9. Gửi mã OTP tới email người dùng để xác thực tài khoản
        emailService.sendOtpEmail(user.getEmail(), otp);
    }


    // ===== 1.2. XÁC THỰC OTP =====
    public void verifyOtp(VerifyOtpRequest request) {
        // 1.2.1. Tìm mã OTP trong DB dựa theo email và mã OTP từ thông tin của request
        UserOtp otp = otpRepository.findByUserEmailAndOtpCode(request.getEmail(), request.getOtpCode());

        // 1.2.2. Kiểm tra OTP có hợp lệ hay không:
        // - Không tồn tại
        // - Đã được sử dụng
        // - Hoặc đã hết hạn
        if (otp == null || otp.isUsed() || otp.getExpiryTime().before(new Timestamp(System.currentTimeMillis()))) {
            //Alternative Flows(1.2.2): OTP không hợp lệ hoặc đã hết hạn
            throw new RuntimeException("OTP không hợp lệ hoặc đã hết hạn");
        }

        // 1.2.3. Đánh dấu OTP này đã được sử dụng
        otp.setUsed(true);

        // 1.2.4. Lưu lại trạng thái OTP đã sử dụng vào DB
        otpRepository.save(otp);

        // 1.2.5. Tìm User tương ứng với email để kích hoạt tài khoản
        User user = userRepository.findByEmail(request.getEmail())
                // Alternative Flows(1.2.5): User không được tìm thấy bằng email trong DB
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với email: " + request.getEmail()));

        // 1.2.6. Kích hoạt tài khoản người dùng
        user.setEnabled(true);

        // 1.2.7. Lưu lại trạng thái tài khoản đã được kích hoạt vào DB
        userRepository.save(user);
    }


    // ===== 2. ĐĂNG NHẬP =====
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Tài khoản chưa được kích hoạt");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu không đúng");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Lưu refreshToken vào DB
        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setUser(user);
        tokenEntity.setToken(refreshToken);
        tokenEntity.setExpiryDate(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)));
        refreshTokenRepository.save(tokenEntity);

        return jwtMapper.toJwtResponse(accessToken, refreshToken);
    }

    public JwtResponse refreshAccessToken(String refreshToken) {
        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token không hợp lệ"));

        if (tokenEntity.getExpiryDate().before(new Timestamp(System.currentTimeMillis()))) {
            refreshTokenRepository.delete(tokenEntity);
            throw new RuntimeException("Refresh token đã hết hạn");
        }

        User user = tokenEntity.getUser();

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // Cập nhật token mới vào DB (tuỳ chọn: xóa token cũ, hoặc cập nhật lại)
        tokenEntity.setToken(newRefreshToken);
        tokenEntity.setExpiryDate(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)));
        refreshTokenRepository.save(tokenEntity);

        return jwtMapper.toJwtResponse(newAccessToken, newRefreshToken);
    }

    // ===== #. ĐĂNG XUẤT =====
    public void logout(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token không hợp lệ"));
        refreshTokenRepository.delete(token); // hoặc đánh dấu là revoked
    }

}
