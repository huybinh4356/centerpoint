package com.project.centerpoint.service;

import com.project.centerpoint.entity.User;

public interface UserService {
    void registerUser(User user);
    boolean existsByEmail(String email);
    void createPasswordResetTokenForUser(User user, String token);
    boolean validatePasswordResetToken(String token);
    void changeUserPassword(User user, String newPassword);
    User getUserByPasswordResetToken(String token);
    java.util.List<User> getAllUsers();
    void updateUserStatus(Long id, boolean locked);
    User getUserByEmail(String email);
    void importUsersFromJson(org.springframework.web.multipart.MultipartFile file) throws java.io.IOException;
    void updateUser(User user);
}
