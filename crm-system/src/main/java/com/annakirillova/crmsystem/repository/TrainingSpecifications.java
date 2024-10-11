package com.annakirillova.crmsystem.repository;

import com.annakirillova.crmsystem.models.Trainee;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.models.Training;
import com.annakirillova.crmsystem.models.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TrainingSpecifications {

    public static Specification<Training> hasTraineeUsername(String traineeUsername) {
        return (root, query, criteriaBuilder) -> {
            if (traineeUsername == null || traineeUsername.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Training, Trainee> traineeJoin = root.join("trainee");
            Join<Trainee, User> userJoin = traineeJoin.join("user");
            return criteriaBuilder.equal(userJoin.get("username"), traineeUsername);
        };
    }

    public static Specification<Training> hasTrainerUsername(String trainerUsername) {
        return (root, query, criteriaBuilder) -> {
            if (trainerUsername == null || trainerUsername.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Training, Trainer> trainerJoin = root.join("trainer");
            Join<Trainer, User> userJoin = trainerJoin.join("user");
            return criteriaBuilder.equal(userJoin.get("username"), trainerUsername);
        };
    }

    public static Specification<Training> isBetweenDates(LocalDate fromDate, LocalDate toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null && toDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (fromDate != null && toDate != null) {
                return criteriaBuilder.between(root.get("date"), fromDate, toDate);
            } else if (fromDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("date"), fromDate);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("date"), toDate);
            }
        };
    }

    public static Specification<Training> hasTrainingType(String trainingType) {
        return (root, query, criteriaBuilder) -> {
            if (trainingType == null || trainingType.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("type").get("name"), trainingType);
        };
    }

    public static Specification<Training> hasTrainerFirstName(String trainerFirstName) {
        return (root, query, criteriaBuilder) -> {
            if (trainerFirstName == null || trainerFirstName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Training, Trainer> trainerJoin = root.join("trainer");
            Join<Trainer, User> userJoin = trainerJoin.join("user");
            return criteriaBuilder.equal(userJoin.get("firstName"), trainerFirstName);
        };
    }

    public static Specification<Training> hasTrainerLastName(String trainerLastName) {
        return (root, query, criteriaBuilder) -> {
            if (trainerLastName == null || trainerLastName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Training, Trainer> trainerJoin = root.join("trainer");
            Join<Trainer, User> userJoin = trainerJoin.join("user");
            return criteriaBuilder.equal(userJoin.get("lastName"), trainerLastName);
        };
    }

    public static Specification<Training> hasTraineeFirstName(String traineeFirstName) {
        return (root, query, criteriaBuilder) -> {
            if (traineeFirstName == null || traineeFirstName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Training, Trainee> traineeJoin = root.join("trainee");
            Join<Trainee, User> userJoin = traineeJoin.join("user");
            return criteriaBuilder.equal(userJoin.get("firstName"), traineeFirstName);
        };
    }

    public static Specification<Training> hasTraineeLastName(String traineeLastName) {
        return (root, query, criteriaBuilder) -> {
            if (traineeLastName == null || traineeLastName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Training, Trainee> traineeJoin = root.join("trainee");
            Join<Trainee, User> userJoin = traineeJoin.join("user");
            return criteriaBuilder.equal(userJoin.get("lastName"), traineeLastName);
        };
    }

    public static Specification<Training> getTraineeTrainings(String traineeUsername, LocalDate fromDate,
                                                              LocalDate toDate, String trainingType,
                                                              String trainerFirstName, String trainerLastName) {
        return TrainingSpecifications.getTrainings(traineeUsername, null, fromDate, toDate, trainingType,
                trainerFirstName, trainerLastName, null, null);
    }

    public static Specification<Training> getTrainerTrainings(String trainerUsername,
                                                              LocalDate fromDate, LocalDate toDate,
                                                              String traineeFirstName, String traineeLastName) {
        return TrainingSpecifications.getTrainings(null, trainerUsername, fromDate, toDate, null,
                null, null, traineeFirstName, traineeLastName);
    }

    public static Specification<Training> getTrainings(String traineeUsername, String trainerUsername, LocalDate fromDate,
                                                       LocalDate toDate, String trainingType, String trainerFirstName,
                                                       String trainerLastName, String traineeFirstName, String traineeLastName) {
        return Specification.where(hasTraineeUsername(traineeUsername))
                .and(hasTrainerUsername(trainerUsername))
                .and(isBetweenDates(fromDate, toDate))
                .and(hasTrainingType(trainingType))
                .and(hasTrainerFirstName(trainerFirstName))
                .and(hasTrainerLastName(trainerLastName))
                .and(hasTraineeFirstName(traineeFirstName))
                .and(hasTraineeLastName(traineeLastName));
    }
}
