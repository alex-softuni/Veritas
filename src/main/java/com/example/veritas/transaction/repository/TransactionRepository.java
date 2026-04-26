package com.example.veritas.transaction.repository;

import com.example.veritas.transaction.model.Transaction;
import com.example.veritas.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findTop5ByOwnerIdOrderByCreatedAtDesc(UUID id);
}
