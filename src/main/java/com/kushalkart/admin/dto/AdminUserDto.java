package com.kushalkart.admin.dto;

import com.kushalkart.admin.entity.AdminUser;

public class AdminUserDto {
    private Long id;
    private String name;
    private String email;
    private String role;

    public AdminUserDto(AdminUser user) {
        this.id = user.getId();
        this.name = user.getUsername();
        this.email = user.getUsername(); // assuming username is email or phone
        this.role = user.getRole().name();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
