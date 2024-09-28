package com.directory.service;

import com.directory.entity.User;
import com.directory.entity.enums.RoleEnum;
import com.directory.repository.UserRepository;
import com.directory.repository.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.directory.entity.Role;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){//}, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        Role role = new Role();
        role.setName(RoleEnum.ADMIN);
        user.setRole(role);
        this.userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public List<User> findAllUsers() {
        List<User> users = this.userRepository.findAll();
        return new ArrayList(users);
    }
}
