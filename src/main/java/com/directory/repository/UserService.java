package com.directory.repository;

import com.directory.entity.User;

import java.util.List;


public interface UserService {
    void saveUser(User userDto);

    User findUserByEmail(String email);

    List<User> findAllUsers();
}
