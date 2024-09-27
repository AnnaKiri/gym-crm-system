package com.annakirillova.trainerworkloadservice.repository;

import com.annakirillova.trainerworkloadservice.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
public interface TrainingRepository extends JpaRepository<Training, Integer> {

    @Query( "SELECT t FROM Training t WHERE t.trainer.username = :username")
    List<Training> getTrainingsByUsername(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM Training t WHERE t.trainer.username = :username AND t.date = :date AND t.duration = :duration")
    int deleteTrainingByDateAndDuration(@Param("username") String username, @Param("date") LocalDate date, @Param("duration") int duration);
}
