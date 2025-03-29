package com.digipay.services;

import com.digipay.rest.dtos.UserDto;
import com.digipay.entities.User;
import com.digipay.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        User user = User.builder()
                .id(User.generateRandomUserId())
                .name(userDto.getName())
                .balance(BigDecimal.valueOf(userDto.getBalance()))
                .build();

        return UserDto.from (userRepository.saveUser(user));
    }

    public UserDto getUserById(String id) {
        return UserDto.from (userRepository.getUserById(id));
    }

    public void updateAllUsers(List<UserDto> userDtos) {

        List<User> usersToUpdate = new ArrayList<>();
        for (UserDto userDto : userDtos) {

            if (userDto.getId() == null) throw new IllegalArgumentException("User id is null");

            User userToUpdate = userRepository.getUserById(userDto.getId());
            if (userToUpdate == null) {
                throw new IllegalArgumentException("Invalid userId");
            }
            System.out.println(userToUpdate);

            userToUpdate.setName(userDto.getName());
            userToUpdate.setBalance(BigDecimal.valueOf(userDto.getBalance()));

            usersToUpdate.add(userToUpdate);
        }
        userRepository.saveAllUser(usersToUpdate);
    }
}
