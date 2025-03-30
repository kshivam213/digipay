package com.digipay.validators;

import com.digipay.rest.DigipayException;
import com.digipay.rest.dtos.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @InjectMocks
    private UserValidator userValidator;

    @Test
    void shouldThrowExceptionWhenUserDtoIsNull() {
        assertThatThrownBy(() -> userValidator.validateUserDetails(null))
                .isInstanceOf(DigipayException.class)
                .hasMessage("Request Body can't be null");
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        UserDto userDto = new UserDto();
        userDto.setName("");
        userDto.setBalance(BigDecimal.valueOf(100));

        assertThatThrownBy(() -> userValidator.validateUserDetails(userDto))
                .isInstanceOf(DigipayException.class)
                .hasMessage("Name can't be null or empty");
    }

    @Test
    void shouldThrowExceptionWhenBalanceIsNegative() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setBalance(BigDecimal.valueOf(-10.0));

        assertThatThrownBy(() -> userValidator.validateUserDetails(userDto))
                .isInstanceOf(DigipayException.class)
                .hasMessage("Balance can't be less than 0");
    }

    @Test
    void shouldNotThrowExceptionForValidUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setBalance(BigDecimal.valueOf(100.0));

        assertThatCode(() -> userValidator.validateUserDetails(userDto))
                .doesNotThrowAnyException();
    }
}
