package com.example.veritas.wallet.service;

import com.example.veritas.user.model.User;
import com.example.veritas.wallet.model.Wallet;
import com.example.veritas.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;


@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet initializeWallet(User user) {

        return walletRepository.save(Wallet.builder()
                .owner(user)
                .balance(BigDecimal.ZERO)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build());
    }

    public BigDecimal getTotalBalance(User user) {
        return user.getWallets().stream()
                .map(Wallet::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}
