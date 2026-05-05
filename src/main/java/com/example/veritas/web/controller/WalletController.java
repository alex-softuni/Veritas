package com.example.veritas.web.controller;

import com.example.veritas.security.AuthenticationDetails;
import com.example.veritas.user.model.User;
import com.example.veritas.user.service.UserService;
import com.example.veritas.wallet.model.Wallet;
import com.example.veritas.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class WalletController {

    private final UserService userService;
    private final WalletService walletService;

    @Autowired
    public WalletController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }


    @GetMapping("/wallets")
    public ModelAndView getWalletPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User user = userService.getUserById(authenticationDetails.getId());
        List<Wallet> wallets = walletService.getWalletsForUser(user);
        BigDecimal totalBalance = walletService.getTotalBalance(user);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("wallet");
        mav.addObject("user", user);
        mav.addObject("wallets", wallets);
        mav.addObject("totalBalance", totalBalance);
        mav.addObject("walletCount", wallets.size());
        return mav;
    }
}
