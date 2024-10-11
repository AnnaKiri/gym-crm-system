package com.annakirillova.crmsystem.repository;

import com.annakirillova.crmsystem.models.Trainee;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.models.Training;
import com.annakirillova.crmsystem.models.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class TrainerSpecifications {

    public static Specification<Trainer> notAssignedToTrainee(String username) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Training> subRoot = subquery.from(Training.class);
            Join<Training, Trainee> subTraineeJoin = subRoot.join("trainee", JoinType.LEFT);
            Join<Trainee, User> subUserJoin = subTraineeJoin.join("user", JoinType.LEFT);
            Join<Training, Trainer> subTrainerJoin = subRoot.join("trainer", JoinType.LEFT);

            subquery.select(subTrainerJoin.get("id"))
                    .where(criteriaBuilder.equal(subUserJoin.get("username"), username));

            return criteriaBuilder.not(root.get("id").in(subquery));
        };
    }
}
