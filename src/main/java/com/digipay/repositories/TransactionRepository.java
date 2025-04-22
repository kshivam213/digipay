package com.digipay.repositories;

import com.digipay.entities.Transaction;

public interface TransactionRepository {
    Transaction saveTransaction(Transaction transaction);
    Transaction getById(String id);
}
