package com.project.centerpoint.service;

import com.project.centerpoint.entity.PasswordResetToken;
import com.project.centerpoint.entity.Role;
import com.project.centerpoint.entity.User;
import com.project.centerpoint.repository.PasswordResetTokenRepository;
import com.project.centerpoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.io.InputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.centerpoint.dto.UserImportDTO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        user.setLocked(false);
        userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDate(LocalDateTime.now().plusMinutes(30)); // 30 minutes expiry
        tokenRepository.save(myToken);
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passToken = tokenRepository.findByToken(token);
        return passToken.isPresent() && !passToken.get().getExpiryDate().isBefore(LocalDateTime.now());
    }

    @Override
    public void changeUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User getUserByPasswordResetToken(String token) {
        return tokenRepository.findByToken(token).map(PasswordResetToken::getUser).orElse(null);
    }

    @Override
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUserStatus(Long id, boolean locked) {
        userRepository.findById(id).ifPresent(user -> {
            user.setLocked(locked);
            userRepository.save(user);
        });
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    @Transactional
    public void importUsersFromJson(MultipartFile file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = file.getInputStream()) {
            List<UserImportDTO> importDTOs = mapper.readValue(inputStream, new TypeReference<List<UserImportDTO>>() {});
            
            for (UserImportDTO dto : importDTOs) {
                if (userRepository.existsByEmail(dto.getEmail())) {
                    continue; // Skip existing users
                }
                User user = new User();
                user.setFullName(dto.getFullName());
                user.setEmail(dto.getEmail());
                user.setPassword(passwordEncoder.encode(dto.getPassword() != null ? dto.getPassword() : "123456"));
                user.setPhone(dto.getPhone());
                user.setRole(Role.valueOf(dto.getRole() != null ? dto.getRole() : "ROLE_USER"));
                user.setGender(dto.getGender());
                user.setAddress(dto.getAddress());
                user.setCccd(dto.getCccd());
                user.setEnabled(true);
                user.setLocked(false);
                user.setEmailVerified(true);
                user.setPhoneVerified(true);
                userRepository.save(user);
            }
        }
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
