package com.digipay.rest;

import com.digipay.rest.dtos.TransactionDto;
import com.digipay.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<ApiResponse<TransactionDto>> makeTransaction(@RequestBody TransactionDto transactionDto) {
        TransactionDto transactionResp = transactionService.makeTransaction(transactionDto);
        ApiResponse<TransactionDto> response = new ApiResponse<>(true, "Successfully made transaction", transactionResp);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<ApiResponse<TransactionDto>> getTransaction(@PathVariable String transactionId) {
        TransactionDto transactionDto = transactionService.getTransaction(transactionId);
        ApiResponse<TransactionDto> response = new ApiResponse<>(true, "Successfully retrieved transaction", transactionDto);
        return ResponseEntity.ok(response);
    }
}
