package com.omarrdev.ithra.repository;

import com.omarrdev.ithra.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndDeletedFalse(String username);
    Optional<User> findByEmailAndDeletedFalse(String email);
    Optional<User> findByEmailVerificationTokenAndDeletedFalse(String token);
    Optional<User> findByResetPasswordTokenAndDeletedFalse(String token);
    boolean existsByUsernameAndDeletedFalse(String username);
    boolean existsByEmailAndDeletedFalse(String email);
    Page<User> findAllByDeletedFalse(Pageable pageable);
    Optional<User> findByIdAndDeletedFalse(Long id);
}
