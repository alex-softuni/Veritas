package com.example.veritas.web.controller;

import com.example.veritas.security.AuthenticationDetails;
import com.example.veritas.user.model.User;
import com.example.veritas.user.model.UserRole;
import com.example.veritas.user.service.UserService;
import com.example.veritas.wallet.model.Wallet;
import com.example.veritas.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    @Test
    void getWalletPageBuildsWalletModelForAuthenticatedUser() {
        UUID userId = UUID.randomUUID();
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, "alex", "secret", UserRole.USER, true);
        User user = User.builder().id(userId).username("alex").build();
        List<Wallet> wallets = List.of(
                Wallet.builder()
                        .id(UUID.randomUUID())
                        .owner(user)
                        .balance(new BigDecimal("250.00"))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        when(userService.getUserById(userId)).thenReturn(user);
        when(walletService.getWalletsForUser(user)).thenReturn(wallets);
        when(walletService.getTotalBalance(user)).thenReturn(new BigDecimal("250.00"));

        ModelAndView result = walletController.getWalletPage(authenticationDetails);

        assertThat(result.getViewName()).isEqualTo("wallet");
        assertThat(result.getModel())
                .containsEntry("user", user)
                .containsEntry("wallets", wallets)
                .containsEntry("totalBalance", new BigDecimal("250.00"))
                .containsEntry("walletCount", 1);
    }
}

