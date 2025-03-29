package com.digipay.repositories;

import com.digipay.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final BaseRepository<User, String> baseRepository;

    @Override
    public User saveUser(User user) {
       return baseRepository.save(user);
    }

    @Override
    public User getUserById(String userId) {
        return baseRepository.getById(userId);
    }

    @Override
    public void saveAllUser(List<User> userList) {
        baseRepository.saveAll(userList);
    }
}