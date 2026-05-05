package com.example.veritas.wallet.service;

import com.example.veritas.user.model.User;
import com.example.veritas.wallet.model.Wallet;
import com.example.veritas.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void getWalletsForUserReturnsWalletsFromRepository() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        List<Wallet> expectedWallets = List.of(buildWallet(user, "125.50"));

        when(walletRepository.findByOwnerIdOrderByCreatedAtAsc(userId)).thenReturn(expectedWallets);

        List<Wallet> result = walletService.getWalletsForUser(user);

        assertThat(result).isSameAs(expectedWallets);
        verify(walletRepository).findByOwnerIdOrderByCreatedAtAsc(userId);
    }

    @Test
    void getTotalBalanceSumsAllWalletBalancesAndIgnoresNulls() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        List<Wallet> wallets = List.of(
                buildWallet(user, "125.50"),
                buildWallet(user, "24.50"),
                Wallet.builder()
                        .id(UUID.randomUUID())
                        .owner(user)
                        .balance(null)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        when(walletRepository.findByOwnerIdOrderByCreatedAtAsc(userId)).thenReturn(wallets);

        BigDecimal result = walletService.getTotalBalance(user);

        assertThat(result).isEqualByComparingTo("150.00");
        verify(walletRepository).findByOwnerIdOrderByCreatedAtAsc(userId);
    }

    private Wallet buildWallet(User owner, String balance) {
        return Wallet.builder()
                .id(UUID.randomUUID())
                .owner(owner)
                .balance(new BigDecimal(balance))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

