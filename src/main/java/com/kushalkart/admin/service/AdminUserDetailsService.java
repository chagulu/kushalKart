package com.kushalkart.admin.service;

import com.kushalkart.admin.entity.AdminUser;
import com.kushalkart.admin.repository.AdminUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    public AdminUserDetailsService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin user not found with username: " + username));

        return User.builder()
                .username(adminUser.getUsername())
                .password(adminUser.getPassword()) // should already be encoded
                .roles(adminUser.getRole().name())  // make sure role is an ENUM or String
                .build();
    }
}
