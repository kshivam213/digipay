package com.digipay.services;

import ch.qos.logback.core.util.StringUtil;
import com.digipay.rest.DigipayException;
import com.digipay.rest.dtos.UserDto;
import com.digipay.entities.User;
import com.digipay.repositories.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {

        if (userDto == null) throw new DigipayException("Request Body can't be null", HttpStatus.BAD_REQUEST);
        if (StringUtils.isEmpty(userDto.getName())) throw new DigipayException("Name can't be null or empty", HttpStatus.BAD_REQUEST);
        if (userDto.getBalance() <= 0) throw new DigipayException("Balance can't be less than 0", HttpStatus.BAD_REQUEST);

        User user = User.builder()
                .id(User.generateRandomUserId())
                .name(userDto.getName())
                .balance(BigDecimal.valueOf(userDto.getBalance()))
                .build();

        return UserDto.from (userRepository.saveUser(user));
    }

    public UserDto getUserById(String id) {

        User user = userRepository.getUserById(id);
        if (user == null) throw new DigipayException("User not found", HttpStatus.NOT_FOUND);

        return UserDto.from (user);
    }

    public void updateAllUsers(List<UserDto> userDtos) {

        List<User> usersToUpdate = new ArrayList<>();
        for (UserDto userDto : userDtos) {

            if (StringUtils.isEmpty(userDto.getId())) throw new DigipayException("User id can't be null or empty", HttpStatus.BAD_REQUEST);
            if (userDto.getBalance() <= 0) throw new DigipayException("Balance can't be less than 0", HttpStatus.BAD_REQUEST);

            User userToUpdate = userRepository.getUserById(userDto.getId());
            if (userToUpdate == null) throw new DigipayException("User not found", HttpStatus.NOT_FOUND);

            userToUpdate.setName(userDto.getName());
            userToUpdate.setBalance(BigDecimal.valueOf(userDto.getBalance()));

            usersToUpdate.add(userToUpdate);
        }
        userRepository.saveAllUser(usersToUpdate);
    }
}
