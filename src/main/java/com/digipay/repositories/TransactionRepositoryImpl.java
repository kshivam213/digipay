package com.digipay.repositories;

import com.digipay.entities.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {
    private final BaseRepository<Transaction, String> baseRepository;

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return baseRepository.save(transaction);
    }

    @Override
    public Transaction getById(String id) {
        return baseRepository.getById(id);
    }
}
