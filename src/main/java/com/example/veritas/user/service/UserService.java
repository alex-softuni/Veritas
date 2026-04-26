package com.example.veritas.user.service;

import com.example.veritas.security.AuthenticationDetails;
import com.example.veritas.user.model.User;
import com.example.veritas.user.model.UserRole;
import com.example.veritas.user.repository.UserRepository;
import com.example.veritas.wallet.model.Wallet;
import com.example.veritas.wallet.service.WalletService;
import com.example.veritas.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final WalletService walletService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, WalletService walletService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.walletService = walletService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return new AuthenticationDetails(user.getId(),user.getUsername(),user.getPassword(),user.getRole(),user.isEnabled());

    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());
        if (optionalUser.isPresent()) {
            throw new UsernameNotFoundException(registerRequest.getUsername());
        }

        User user = userRepository.save(initializeUser(registerRequest));

        Wallet wallet = walletService.initializeWallet(user);

        user.setWallets(List.of(wallet));

    }

    private User initializeUser(RegisterRequest registerRequest) {
        String encodedPass = passwordEncoder.encode(registerRequest.getPassword());

        return User.builder()
                .username(registerRequest.getUsername())
                .password(encodedPass)
                .email(registerRequest.getEmail())
                .role(UserRole.USER)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }
}
