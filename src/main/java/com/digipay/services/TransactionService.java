package com.digipay.services;

import com.digipay.entities.Transaction;
import com.digipay.entities.TransactionStatus;
import com.digipay.entities.User;
import com.digipay.repositories.TransactionRepository;
import com.digipay.rest.dtos.TransactionDto;
import com.digipay.rest.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public TransactionDto makeTransaction(TransactionDto transactionDto) {

        UserDto sender = userService.getUserById(transactionDto.getSenderId());
        UserDto receiver  = userService.getUserById(transactionDto.getReceiverId());

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString().replace("-", "").substring(0, 14))
                .senderId(sender.getId())
                .receiverId(receiver.getId())
                .amount(BigDecimal.valueOf(transactionDto.getAmount()))
                .build();

        sender.setBalance(sender.getBalance() - transactionDto.getAmount());
        receiver.setBalance(receiver.getBalance() + transactionDto.getAmount());

        userService.updateAllUsers(Arrays.asList(sender, receiver));

        transaction.setStatus(TransactionStatus.SUCCESS);

        return TransactionDto.from(transactionRepository.saveTransaction(transaction));
    }
}
