package com.digipay.services;

import com.digipay.entities.Transaction;
import com.digipay.entities.TransactionStatus;
import com.digipay.entities.User;
import com.digipay.repositories.TransactionRepository;
import com.digipay.rest.DigipayException;
import com.digipay.rest.dtos.TransactionDto;
import com.digipay.rest.dtos.UserDto;
import com.digipay.validators.TransactionValidator;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final TransactionValidator transactionValidator;

    public TransactionDto makeTransaction(TransactionDto transactionDto) {

        transactionValidator.validateTransaction(transactionDto);

        UserDto sender = userService.getUserById(transactionDto.getSenderId());
        UserDto receiver  = userService.getUserById(transactionDto.getReceiverId());

        if (sender.getBalance() < transactionDto.getAmount()) throw new DigipayException("Insufficient funds in your account", HttpStatus.BAD_REQUEST);

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString().replace("-", "").substring(0, 14))
                .senderId(sender.getId())
                .receiverId(receiver.getId())
                .amount(BigDecimal.valueOf(transactionDto.getAmount()))
                .build();
        try {
            sender.setBalance(sender.getBalance() - transactionDto.getAmount());
            receiver.setBalance(receiver.getBalance() + transactionDto.getAmount());

            userService.updateAllUsers(Arrays.asList(sender, receiver));

            transaction.setStatus(TransactionStatus.SUCCESS);
        } catch (Exception ex) {
            throw new DigipayException("Failed to execute transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return TransactionDto.from(transactionRepository.saveTransaction(transaction));
    }
}
