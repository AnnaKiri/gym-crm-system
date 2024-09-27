package com.annakirillova.crmsystem.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_attempt")
@Getter
@Setter
@NoArgsConstructor
public class LoginAttempt extends AbstractBaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "attempts", nullable = false)
    private int attempts;

    @Column(name = "blocked_until")
    private LocalDateTime blockedUntil;

    @Override
    public String toString() {
        return "LoginAttempt{" +
                "user=" + user.getUsername() +
                ", attempts=" + attempts +
                ", blockedUntil=" + blockedUntil +
                '}';
    }
}
