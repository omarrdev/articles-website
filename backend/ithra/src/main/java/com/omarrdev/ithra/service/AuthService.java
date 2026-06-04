package com.omarrdev.ithra.service;

import com.omarrdev.ithra.dto.request.*;
import com.omarrdev.ithra.dto.response.AuthResponse;
import com.omarrdev.ithra.entity.User;
import com.omarrdev.ithra.enums.Role;
import com.omarrdev.ithra.exception.BusinessException;
import com.omarrdev.ithra.exception.ResourceNotFoundException;
import com.omarrdev.ithra.mapper.UserMapper;
import com.omarrdev.ithra.repository.UserRepository;
import com.omarrdev.ithra.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsernameAndDeletedFalse(request.getUsername())) {
            throw new BusinessException("Username already taken");
        }
        if (userRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
            throw new BusinessException("Email already registered");
        }

        String verificationToken = UUID.randomUUID().toString();
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.READER)
                .emailVerified(false)
                .emailVerificationToken(verificationToken)
                .emailVerificationTokenExpiry(LocalDateTime.now().plusHours(24))
                .tokenVersion(0)
                .build();

        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), verificationToken);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameAndDeletedFalse(request.getUsernameOrEmail())
                .or(() -> userRepository.findByEmailAndDeletedFalse(request.getUsernameOrEmail()))
                .orElseThrow(() -> new BusinessException("Invalid credentials", HttpStatus.UNAUTHORIZED));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword()));

        return buildAuthResponse(user);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String token = request.getRefreshToken();
        if (!jwtService.validateToken(token) || !jwtService.isRefreshToken(token)) {
            throw new BusinessException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        String username = jwtService.extractUsername(token);
        int tokenVersion = jwtService.extractTokenVersion(token);

        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.UNAUTHORIZED));

        if (user.getTokenVersion() != tokenVersion) {
            throw new BusinessException("Token has been invalidated", HttpStatus.UNAUTHORIZED);
        }

        return buildAuthResponse(user);
    }

    @Transactional
    public void verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationTokenAndDeletedFalse(token)
                .orElseThrow(() -> new BusinessException("Invalid verification token"));

        if (user.getEmailVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Verification token has expired");
        }

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationTokenExpiry(null);
        userRepository.save(user);
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmailAndDeletedFalse(request.getEmail()).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            user.setResetPasswordToken(token);
            user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));
            userRepository.save(user);
            emailService.sendForgotPasswordEmail(user.getEmail(), user.getUsername(), token);
        });
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetPasswordTokenAndDeletedFalse(request.getToken())
                .orElseThrow(() -> new BusinessException("Invalid reset token"));

        if (user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepository.save(user);
        emailService.sendPasswordChangedEmail(user.getEmail(), user.getUsername());
    }

    @Transactional
    public void resendVerification(ResendVerificationRequest request) {
        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isEmailVerified()) {
            throw new BusinessException("Email already verified");
        }

        String token = UUID.randomUUID().toString();
        user.setEmailVerificationToken(token);
        user.setEmailVerificationTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), token);
    }

    private AuthResponse buildAuthResponse(User user) {
        return new AuthResponse(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user),
                userMapper.toResponse(user)
        );
    }
}
