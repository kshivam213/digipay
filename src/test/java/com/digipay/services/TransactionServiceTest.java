package com.digipay.services;

import com.digipay.entities.Transaction;
import com.digipay.entities.TransactionStatus;
import com.digipay.entities.User;
import com.digipay.repositories.TransactionRepository;
import com.digipay.repositories.UserRepository;
import com.digipay.rest.DigipayException;
import com.digipay.rest.dtos.TransactionDto;
import com.digipay.validators.TransactionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserRepository userRepository;;
    @Mock
    private TransactionValidator transactionValidator;

    @InjectMocks
    private TransactionService transactionService;

    private User userA, userB;
    private User sender;
    private User receiver;


    @BeforeEach
    void setUp() {
        userA = User.builder()
                .id("A")
                .balance(new BigDecimal("1000.00"))
                .build();

        userB = User.builder()
                .id("B")
                .balance(new BigDecimal("1000.00"))
                .build();

        sender = User.builder()
                .id("1001")
                .name("Alice")
                .balance(new BigDecimal("5000"))
                .build();
        receiver = User.builder()
                .id("1002")
                .name("Bob")
                .balance(new BigDecimal("5000"))
                .build();;
    }

    @Test
    void testTransactionCreatedSuccessfully() {
        // Mock repository calls

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSenderId("A");
        transactionDto.setReceiverId("B");
        transactionDto.setAmount(new BigDecimal("500.00"));

        when(userRepository.getUserById("A")).thenReturn(userA);
        when(userRepository.getUserById("B")).thenReturn(userB);
        when(transactionRepository.saveTransaction(any())).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            t.setStatus(TransactionStatus.SUCCESS);
            return t;
        });

        // Execute method
        TransactionDto result = transactionService.makeTransaction(transactionDto);

        // Verify and assert
        assertEquals(TransactionStatus.SUCCESS, result.getStatus());
        assertEquals(new BigDecimal("500.00"), userB.getBalance().subtract(new BigDecimal("1000.00")));
        assertEquals(new BigDecimal("500.00"), new BigDecimal("1000.00").subtract(userA.getBalance()));

        verify(transactionValidator).validateTransaction(transactionDto);
        verify(userRepository, times(1)).saveAllUser(Arrays.asList(userA, userB));
        verify(transactionRepository, times(1)).saveTransaction(any());
    }

    @Test
    void testTransactionFailsDueToInsufficientFunds() {
        userA.setBalance(new BigDecimal("100.00"));

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSenderId("A");
        transactionDto.setReceiverId("B");
        transactionDto.setAmount(new BigDecimal("500.00"));

        when(userRepository.getUserById("A")).thenReturn(userA);
        when(userRepository.getUserById("B")).thenReturn(userB);

        DigipayException exception = assertThrows(DigipayException.class, () -> {
            transactionService.makeTransaction(transactionDto);
        });

        assertEquals("Insufficient funds in your account", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        verify(transactionRepository, never()).saveTransaction(any());
    }

    @Test
    void testTransactionFailsDueToExceptionDuringExecution() {

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSenderId("A");
        transactionDto.setReceiverId("B");
        transactionDto.setAmount(new BigDecimal("500.00"));


        when(userRepository.getUserById("A")).thenReturn(userA);
        when(userRepository.getUserById("B")).thenReturn(userB);

        // Simulate exception when saving users
        doThrow(new RuntimeException("DB error")).when(userRepository).saveAllUser(any());

        DigipayException exception = assertThrows(DigipayException.class, () -> {
            transactionService.makeTransaction(transactionDto);
        });

        assertEquals("Failed to execute transaction", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());

        verify(userRepository, times(1)).saveAllUser(any());
        verify(transactionRepository, never()).saveTransaction(any());
    }

    @Test
    void testBidirectionalConcurrentTransactions() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        when(userRepository.getUserById("1001")).thenReturn(sender);
        when(userRepository.getUserById("1002")).thenReturn(receiver);
        when(transactionRepository.saveTransaction(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Runnable transactionAToB = () -> {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setSenderId("1001");
            transactionDto.setReceiverId("1002");
            transactionDto.setAmount(new BigDecimal("500"));

            transactionService.makeTransaction(transactionDto);
        };

        Runnable transactionBToA = () -> {
            TransactionDto transactionDto1 = new TransactionDto();
            transactionDto1.setSenderId("1002");
            transactionDto1.setReceiverId("1001");
            transactionDto1.setAmount(new BigDecimal("500"));

            transactionService.makeTransaction(transactionDto1);
        };

        for (int i = 0; i < numberOfThreads / 2; i++) {
            executorService.submit(transactionAToB);
            executorService.submit(transactionBToA);
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(BigDecimal.valueOf(5000), sender.getBalance());
        assertEquals(BigDecimal.valueOf(5000), receiver.getBalance());
    }
}
