package com.kirillova.gymcrmsystem.repository;

import com.kirillova.gymcrmsystem.error.NotFoundException;
import com.kirillova.gymcrmsystem.models.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface TrainingRepository extends JpaRepository<Training, Integer>, JpaSpecificationExecutor<Training> {

    @Query("SELECT tr FROM Training tr JOIN FETCH tr.trainee t JOIN FETCH t.user tu JOIN FETCH tr.trainer r JOIN FETCH r.user ru JOIN FETCH tr.type tp WHERE tr.id = :id")
    Optional<Training> getFullTrainingById(@Param("id") int id);

    default Training getExisted(int id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Training with id=" + id + " not found"));
    }
}
