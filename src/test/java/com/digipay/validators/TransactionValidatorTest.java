package com.digipay.validators;

import com.digipay.rest.DigipayException;
import com.digipay.rest.dtos.TransactionDto;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class TransactionValidatorTest {


    @InjectMocks
    private TransactionValidator transactionValidator;

    @Test
    void shouldThrowExceptionWhenTransactionDtoIsNull() {
        DigipayException exception = assertThrows(DigipayException.class, () ->
                transactionValidator.validateTransaction(null));
        assertEquals("Request body can't be null", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenSenderIdIsEmpty() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSenderId("");
        transactionDto.setReceiverId("receiver123");
        transactionDto.setAmount(BigDecimal.valueOf(100));

        DigipayException exception = assertThrows(DigipayException.class, () ->
                transactionValidator.validateTransaction(transactionDto));
        assertEquals("Sender Id can't be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenReceiverIdIsEmpty() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSenderId("sender123");
        transactionDto.setReceiverId("");
        transactionDto.setAmount(BigDecimal.valueOf(100));

        DigipayException exception = assertThrows(DigipayException.class, () ->
                transactionValidator.validateTransaction(transactionDto));
        assertEquals("Receiver Id can't be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsZeroOrNegative() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSenderId("sender123");
        transactionDto.setReceiverId("receiver123");
        transactionDto.setAmount(BigDecimal.valueOf(0));

        DigipayException exception = assertThrows(DigipayException.class, () ->
                transactionValidator.validateTransaction(transactionDto));
        assertEquals("Amount can't be less than 0", exception.getMessage());
    }

    @Test
    void shouldPassWhenTransactionIsValid() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSenderId("sender123");
        transactionDto.setReceiverId("receiver123");
        transactionDto.setAmount(BigDecimal.valueOf(100.0));

        assertDoesNotThrow(() -> transactionValidator.validateTransaction(transactionDto));
    }
}
