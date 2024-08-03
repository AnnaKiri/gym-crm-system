package com.kirillova.gymcrmsystem.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "invalid_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidToken extends AbstractBaseEntity {

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "invalidated_at", nullable = false)
    private LocalDateTime invalidatedAt;

    @Override
    public String toString() {
        return "InvalidToken{" +
                "token='" + token + '\'' +
                ", invalidatedAt=" + invalidatedAt +
                '}';
    }
}
