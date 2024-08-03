package com.kirillova.gymcrmsystem.repository;

import com.kirillova.gymcrmsystem.models.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, Integer> {

    boolean existsByToken(String token);
}
