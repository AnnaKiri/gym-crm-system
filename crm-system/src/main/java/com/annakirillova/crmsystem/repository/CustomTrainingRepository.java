package com.annakirillova.crmsystem.repository;

import com.annakirillova.crmsystem.models.Training;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CustomTrainingRepository {
    List<Training> findAllWithDetails(Specification<Training> spec);
}
