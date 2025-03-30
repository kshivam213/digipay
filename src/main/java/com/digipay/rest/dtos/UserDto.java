package com.digipay.rest.dtos;

import com.digipay.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDto {

    private String id;
    private String name;
    @JsonProperty("balance")
    private BigDecimal balance;
    private String currency;

    public static UserDto from(User user) {
        UserDto dto = new UserDto();
        dto.id = user.getId();
        dto.name = user.getName();
        dto.balance = user.getBalance();
        dto.currency = user.getCurrency().getCurrencyCode();
        return dto;
    }
}
