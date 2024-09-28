package com.directory.service;

import com.directory.entity.User;
import com.directory.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.directory.entity.Role;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


//@Service
@Slf4j
public class CustomUserDetailsService {//implements UserDetailsService {
//    private final UserRepository userRepository;
//
//    @Autowired
//    public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = this.userRepository.findByEmail(email);
//        if (user != null) {
//            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), this.mapRolesToAuthorities(Collections.singleton(user.getRole())));
//        } else {
//            throw new UsernameNotFoundException("Invalid username or password.");
//        }
//    }
//
//    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
//        Collection<? extends GrantedAuthority> mapRoles = (Collection)roles.stream().map((role) -> {
//            return new SimpleGrantedAuthority(role.getName().name());
//        }).collect(Collectors.toList());
//        return mapRoles;
//    }
}
