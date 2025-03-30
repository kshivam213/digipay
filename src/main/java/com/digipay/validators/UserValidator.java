package com.digipay.validators;

import com.digipay.rest.DigipayException;
import com.digipay.rest.dtos.UserDto;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateUserDetails(UserDto userDto) {
        if (userDto == null) throw new DigipayException("Request Body can't be null", HttpStatus.BAD_REQUEST);
        if (StringUtils.isEmpty(userDto.getName())) throw new DigipayException("Name can't be null or empty", HttpStatus.BAD_REQUEST);
        if (userDto.getBalance().doubleValue() <= 0) throw new DigipayException("Balance can't be less than 0", HttpStatus.BAD_REQUEST);
    }
}
