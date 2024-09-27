package com.annakirillova.crmsystem.repository;

import com.annakirillova.crmsystem.error.NotFoundException;
import com.annakirillova.crmsystem.models.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {

    default TrainingType getTrainingTypeIfExists(int id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("Training type id=" + id + " does not exist"));
    }
}
