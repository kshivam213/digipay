package com.digipay.entities;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Transaction {
    private String id;
    private BigDecimal amount;
    private String senderId;
    private String receiverId;

    private TransactionStatus status;
}
