package com.example.veritas.web.controller;

import com.example.veritas.user.service.UserService;
import com.example.veritas.web.dto.LoginRequest;
import com.example.veritas.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController {
    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("register");
        mav.addObject("registerRequest", new RegisterRequest());
        return mav;
    }

    @PostMapping("/register")
    public String registerUser(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/register";
        }

        userService.register(registerRequest);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(value = "error", required = false) String errorParam) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        mav.addObject("loginRequest", new LoginRequest());

        if (errorParam != null) {
            mav.addObject("errorMessage", "Incorrect username or password!");
        }

        return mav;
    }
}
