package com.uas.java1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.uas.java1.model.TokenResetPassword;

import java.util.Optional;

@Transactional
public interface TokenResetPasswordRepository extends JpaRepository<TokenResetPassword, Integer> {
    Optional<TokenResetPassword> findByUsername(String username);
    void deleteByUsername(String username);
}