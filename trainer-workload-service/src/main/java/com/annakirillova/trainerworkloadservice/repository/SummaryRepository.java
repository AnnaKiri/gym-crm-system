package com.annakirillova.trainerworkloadservice.repository;

import com.annakirillova.trainerworkloadservice.model.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface SummaryRepository extends JpaRepository<Summary, Integer> {

    @Query("SELECT t FROM Summary t WHERE t.trainer.username = :username")
    List<Summary> findAllByTrainerUsername(@Param("username") String username);

    @Query("SELECT t FROM Summary t WHERE t.trainer.username = :username AND t.year = :year AND t.month = :month")
    Optional<Summary> findByTrainerAndDate(@Param("username") String username, @Param("year") int year, @Param("month") int month);

    @Modifying
    @Query("UPDATE Summary t SET t.duration = CASE WHEN t.duration - :duration < 0 THEN 0 ELSE t.duration - :duration END WHERE t.trainer.username = :username AND t.year = :year AND t.month = :month")
    int deleteTrainingDurationFromSummaryByDateAndUsername(@Param("username") String username, @Param("year") int year, @Param("month") int month, @Param("duration") int duration);
}
