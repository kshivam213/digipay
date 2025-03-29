package com.digipay.entities;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@ToString
public class User {

    private String id;
    private String name;
    private BigDecimal balance;

    public static String generateRandomUserId() {
        String uuid= UUID.randomUUID().toString().replace("-", "");
        return  uuid.substring(0, 14);
    }
}
