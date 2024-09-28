package com.directory.service.test;

import com.directory.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final User user; // The User entity

    // Constructor that accepts your entity
    public CustomUserDetails(User user) {
        this.user = user;
    }

    // Override all the methods of UserDetails interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You may return roles/authorities from your User entity
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
        return true;
    }
}
