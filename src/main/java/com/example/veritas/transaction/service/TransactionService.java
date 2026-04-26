package com.example.veritas.transaction.service;

import com.example.veritas.transaction.model.Transaction;
import com.example.veritas.transaction.repository.TransactionRepository;
import com.example.veritas.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getRecentTransactions(User owner) {
        return transactionRepository.findTop5ByOwnerIdOrderByCreatedAtDesc(owner.getId());
    }
}
