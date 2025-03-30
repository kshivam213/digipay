package com.digipay.validators;

import com.digipay.rest.DigipayException;
import com.digipay.rest.dtos.TransactionDto;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;

@Component
public class TransactionValidator {

    public void validateTransaction(TransactionDto transactionDto) {
        if (transactionDto == null) {
            throw new DigipayException("Request body can't be null", HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(transactionDto.getSenderId())) {
            throw new DigipayException("Sender Id can't be null or empty", HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(transactionDto.getReceiverId())) {
            throw new DigipayException("Receiver Id can't be null or empty", HttpStatus.BAD_REQUEST);
        }
        if (transactionDto.getAmount().doubleValue() <= 0) {
            throw new DigipayException("Amount can't be less than 0", HttpStatus.BAD_REQUEST);
        }
    }
}
