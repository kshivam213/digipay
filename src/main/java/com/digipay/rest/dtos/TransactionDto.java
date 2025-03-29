package com.digipay.rest.dtos;

import com.digipay.entities.Transaction;
import com.digipay.entities.TransactionStatus;
import lombok.Data;

@Data
public class TransactionDto {

    private String transactionId;
    private String senderId;
    private String receiverId;
    private Double amount;
    private TransactionStatus status;

    public static TransactionDto from(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.amount = transaction.getAmount().doubleValue();
        dto.senderId = transaction.getSenderId();
        dto.receiverId = transaction.getReceiverId();
        dto.transactionId = transaction.getId();
        dto.status = transaction.getStatus();
        return dto;
    }
}
