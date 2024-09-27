package com.annakirillova.crmsystem.repository;

import com.annakirillova.crmsystem.models.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomTrainingRepositoryImpl implements CustomTrainingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Training> findAllWithDetails(Specification<Training> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> root = query.from(Training.class);

        root.fetch("trainee").fetch("user");
        root.fetch("trainer").fetch("user");
        root.fetch("type");

        query.select(root).where(spec.toPredicate(root, query, cb));

        return entityManager.createQuery(query).getResultList();
    }
}
