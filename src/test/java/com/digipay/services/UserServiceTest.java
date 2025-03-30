package com.digipay.services;

import com.digipay.entities.User;
import com.digipay.repositories.UserRepository;
import com.digipay.rest.DigipayException;
import com.digipay.rest.dtos.UserDto;
import com.digipay.validators.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setBalance(new BigDecimal("1000.00"));
        userDto.setId("123");
        userDto.setName("John Doe");

        user = User.builder()
                .id("123")
                .balance(BigDecimal.valueOf(1000.00))
                .currency(Currency.getInstance("USD"))
                .name("John Doe")
                .build();
    }

    @Test
    void testCreateUser() {
        doNothing().when(userValidator).validateUserDetails(userDto);
        when(userRepository.saveUser(any(User.class))).thenReturn(user);

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getBalance(), createdUser.getBalance());

        verify(userValidator, times(1)).validateUserDetails(userDto);
        verify(userRepository, times(1)).saveUser(any(User.class));
    }

    @Test
    void testGetUserById_UserExists() {
        when(userRepository.getUserById("123")).thenReturn(user);

        UserDto foundUser = userService.getUserById("123");

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getBalance(), foundUser.getBalance());
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.getUserById("999")).thenReturn(null);

        DigipayException exception = assertThrows(DigipayException.class, () -> userService.getUserById("999"));

        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testUpdateAllUsers_Success() {
        List<UserDto> userDtos = new ArrayList<>();

        UserDto userDto1 = new UserDto();
        userDto1.setBalance(new BigDecimal("2000.00"));
        userDto1.setId("123");
        userDto1.setName("Updated Name");
        userDtos.add(userDto1);


        when(userRepository.getUserById("123")).thenReturn(user);

        userService.updateAllUsers(userDtos);

        verify(userRepository, times(1)).saveAllUser(anyList());
    }

    @Test
    void testUpdateAllUsers_UserNotFound() {
        List<UserDto> userDtos = new ArrayList<>();

        UserDto userDto1 = new UserDto();
        userDto1.setBalance(new BigDecimal("2000.00"));
        userDto1.setId("999");
        userDto1.setName("Updated Name");
        userDtos.add(userDto1);


        when(userRepository.getUserById("999")).thenReturn(null);

        DigipayException exception = assertThrows(DigipayException.class, () -> userService.updateAllUsers(userDtos));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testUpdateAllUsers_InvalidBalance() {
        List<UserDto> userDtos = new ArrayList<>();
        UserDto userDto1 = new UserDto();
        userDto1.setBalance(new BigDecimal("-100"));
        userDto1.setId("999");
        userDto1.setName("Updated Name");
        userDtos.add(userDto1);


        DigipayException exception = assertThrows(DigipayException.class, () -> userService.updateAllUsers(userDtos));
        assertEquals("Balance can't be less than 0", exception.getMessage());
    }
}