package com.digipay.rest.dtos;

import com.digipay.entities.Transaction;
import com.digipay.entities.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {

    private String transactionId;
    private String senderId;
    private String receiverId;
    private BigDecimal amount;
    private TransactionStatus status;
    private String remark;

    public static TransactionDto from(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.amount = transaction.getAmount();
        dto.senderId = transaction.getSenderId();
        dto.receiverId = transaction.getReceiverId();
        dto.transactionId = transaction.getId();
        dto.status = transaction.getStatus();
        return dto;
    }
}
