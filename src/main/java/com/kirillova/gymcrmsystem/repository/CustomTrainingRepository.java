package com.kirillova.gymcrmsystem.repository;

import com.kirillova.gymcrmsystem.models.Training;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CustomTrainingRepository {
    List<Training> findAllWithDetails(Specification<Training> spec);
}
