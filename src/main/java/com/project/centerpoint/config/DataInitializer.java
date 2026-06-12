package com.project.centerpoint.config;

import com.project.centerpoint.entity.Role;
import com.project.centerpoint.entity.User;
import com.project.centerpoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            jdbcTemplate.execute("ALTER TABLE reviews ADD COLUMN admin_reply TEXT");
            System.out.println("Thêm cột admin_reply thành công!");
        } catch (Exception e) {
            System.out.println("Cột admin_reply đã tồn tại hoặc có lỗi: " + e.getMessage());
        }

        try {
            jdbcTemplate.execute("ALTER TABLE reviews ADD COLUMN updated_at DATETIME");
            System.out.println("Thêm cột updated_at thành công!");
        } catch (Exception e) {
            System.out.println("Cột updated_at đã tồn tại hoặc có lỗi: " + e.getMessage());
        }

        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User admin = new User();
            admin.setFullName("Quản Trị Viên");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRole(Role.ROLE_ADMIN);
            admin.setEnabled(true);
            admin.setLocked(false);
            userRepository.save(admin);
            System.out.println("=====================================");
            System.out.println("Tạo thành công tài khoản Admin mặc định:");
            System.out.println("Email: admin@gmail.com");
            System.out.println("Mật khẩu: 123456");
            System.out.println("=====================================");
        }
    }
}
