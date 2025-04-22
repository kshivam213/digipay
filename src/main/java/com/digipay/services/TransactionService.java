package com.digipay.services;

import com.digipay.entities.Transaction;
import com.digipay.entities.TransactionStatus;
import com.digipay.entities.User;
import com.digipay.repositories.TransactionRepository;
import com.digipay.repositories.UserRepository;
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
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionValidator transactionValidator;

    public TransactionDto makeTransaction(TransactionDto transactionDto) {

        transactionValidator.validateTransaction(transactionDto);

        User sender = userRepository.getUserById(transactionDto.getSenderId());
        User receiver  = userRepository.getUserById(transactionDto.getReceiverId());

        List<User> users = Arrays.asList(sender, receiver);
        users.sort(Comparator.comparing(User::getId));

        if (sender.getBalance().doubleValue() < transactionDto.getAmount().doubleValue()) throw new DigipayException("Insufficient funds in your account", HttpStatus.BAD_REQUEST);

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID().toString().replace("-", "").substring(0, 14))
                .senderId(sender.getId())
                .receiverId(receiver.getId())
                .amount(transactionDto.getAmount())
                .build();
        try {

            users.get(0).lock();
            users.get(1).lock();

            sender.debit(transactionDto.getAmount());
            receiver.credit(transactionDto.getAmount());

            userRepository.saveAllUser(Arrays.asList(sender, receiver));

            transaction.setStatus(TransactionStatus.SUCCESS);
        } catch (Exception ex) {
            throw new DigipayException("Failed to execute transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            users.get(1).unlock();
            users.get(0).unlock();
        }

        return TransactionDto.from(transactionRepository.saveTransaction(transaction));
    }

    public TransactionDto getTransaction(String transactionId) {
        Transaction transaction = transactionRepository.getById(transactionId);
        if (transaction == null) {
            throw new DigipayException("Transaction not found", HttpStatus.NOT_FOUND);
        }
        return TransactionDto.from(transaction);
    }
}
