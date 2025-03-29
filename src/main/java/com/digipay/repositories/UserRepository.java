package com.digipay.repositories;

import com.digipay.entities.User;

import java.util.List;

public interface UserRepository {
    User saveUser(User user);
    User getUserById(String userId);
    void saveAllUser(List<User> user);
}
