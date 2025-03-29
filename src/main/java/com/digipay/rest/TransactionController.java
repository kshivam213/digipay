package com.digipay.rest;

import com.digipay.rest.dtos.TransactionDto;
import com.digipay.rest.dtos.UserDto;
import com.digipay.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public TransactionDto makeTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionService.makeTransaction(transactionDto);
    }
}
