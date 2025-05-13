package ntq.cinema.auth_module.repository;

import ntq.cinema.auth_module.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 1.1.1. Kiểm tra User bằng email đã tồn tại trong DB chưa
    boolean existsByEmail(String email);

    // 1.2.5. Tìm User tương ứng với email để kích hoạt tài khoản
    Optional<User> findByEmail(String email);
}
