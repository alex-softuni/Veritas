package com.example.veritas.web.controller;

import com.example.veritas.security.AuthenticationDetails;
import com.example.veritas.transaction.model.Transaction;
import com.example.veritas.transaction.service.TransactionService;
import com.example.veritas.user.model.User;
import com.example.veritas.user.service.UserService;
import com.example.veritas.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;
    private final WalletService walletService;
    private final TransactionService transactionService;

    @Autowired
    public HomeController(UserService userService, WalletService walletService, TransactionService transactionService) {
        this.userService = userService;
        this.walletService = walletService;
        this.transactionService = transactionService;
    }


    @GetMapping("/home")
    public ModelAndView getHomePage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails){

        User user = userService.getUserById(authenticationDetails.getId());
        BigDecimal balance = walletService.getTotalBalance(user);
        List<Transaction> recentTransactions = transactionService.getRecentTransactions(user);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");
        mav.addObject("user", user);
        mav.addObject("balance", balance);
        mav.addObject("recentTransactions", recentTransactions);

        return mav;
    }
}
