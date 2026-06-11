package com.project.centerpoint.dto;

import lombok.Data;

@Data
public class UserImportDTO {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String role; // ROLE_USER or ROLE_ADMIN
    private String gender;
    private String address;
    private String cccd;
}
