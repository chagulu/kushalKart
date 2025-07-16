package com.kushalkart.admin.dto;

import com.kushalkart.admin.entity.AdminUser;

public class LoginResponse {
    private String token;
    private AdminUserDto admin;

    public LoginResponse(String token, AdminUser user) {
        this.token = token;
        this.admin = new AdminUserDto(user);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AdminUserDto getAdmin() {
        return admin;
    }

    public void setAdmin(AdminUserDto admin) {
        this.admin = admin;
    }
}
