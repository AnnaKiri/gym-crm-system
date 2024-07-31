package com.kirillova.gymcrmsystem.repository;

import com.kirillova.gymcrmsystem.error.NotFoundException;
import com.kirillova.gymcrmsystem.models.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {

    default TrainingType getExisted(int id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Training type id=" + id + " does not exist"));
    }
}
