package com.kushalkart.model;

import com.kushalkart.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getUserId() {
        return user.getId(); // ✅ Add this to expose userId
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole();

        if (role != null && !role.trim().isEmpty()) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
        }

        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null; // or user.getOtp() if needed
    }

    @Override
    public String getUsername() {
        return user.getMobile(); // mobile used as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true; // or return user.isVerified();
    }
}
