package com.digipay.entities;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Currency;
import java.util.concurrent.locks.ReentrantLock;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@ToString
public class User {

    private String id;
    private String name;
    private BigDecimal balance;
    private Currency currency;
    private final ReentrantLock lock = new ReentrantLock();

    public static String generateRandomUserId() {
        String uuid= UUID.randomUUID().toString().replace("-", "");
        return  uuid.substring(0, 14);
    }

    public void debit(BigDecimal amount) {
        this.balance = balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        this.balance = balance.add(amount);
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

}
