package com.annakirillova.crmsystem.repository;

import com.annakirillova.crmsystem.models.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    @Query("SELECT la FROM LoginAttempt la WHERE la.user.username = :username")
    Optional<LoginAttempt> findByUsername(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM LoginAttempt la WHERE la.user.username = :username")
    int deleteByUsername(@Param("username") String username);
}
