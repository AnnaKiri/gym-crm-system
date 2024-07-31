package com.kirillova.gymcrmsystem.repository;

import com.kirillova.gymcrmsystem.error.NotFoundException;
import com.kirillova.gymcrmsystem.models.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface TrainerRepository extends JpaRepository<Trainer, Integer>, JpaSpecificationExecutor<Trainer> {

    @Query("SELECT DISTINCT trn FROM Trainee t JOIN t.trainerList trn JOIN FETCH trn.user JOIN FETCH trn.specialization WHERE t.user.username = :username")
    List<Trainer> findTrainersByTraineeUsername(@Param("username") String username);

    @Query("SELECT t FROM Trainer t JOIN FETCH t.user u LEFT JOIN FETCH t.specialization s  WHERE u.username = :username")
    Optional<Trainer> getWithUserAndSpecialization(@Param("username") String username);

    @Query("SELECT t FROM Trainer t JOIN t.user u WHERE u.username = :username")
    Optional<Trainer> findByUsername(@Param("username") String username);

    default Trainer getExisted(String username) {
        return findByUsername(username).orElseThrow(() -> new NotFoundException("Trainer with username=" + username + " not found"));
    }
}
