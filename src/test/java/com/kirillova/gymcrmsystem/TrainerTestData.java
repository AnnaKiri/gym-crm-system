package com.kirillova.gymcrmsystem;

import com.kirillova.gymcrmsystem.models.Trainer;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.to.TrainerTo;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_TO_1;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_TO_2;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_TO_3;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_TO_4;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_1;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_1_ID;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_2;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_3;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_4;
import static com.kirillova.gymcrmsystem.UserTestData.USER_5;
import static com.kirillova.gymcrmsystem.UserTestData.USER_6;
import static com.kirillova.gymcrmsystem.UserTestData.USER_7;
import static com.kirillova.gymcrmsystem.UserTestData.USER_8;
import static com.kirillova.gymcrmsystem.UserTestData.USER_9;
import static org.assertj.core.api.Assertions.assertThat;

public class TrainerTestData {
    public static final int TRAINER_1_ID = 1;

    public static final Trainer TRAINER_1 = new Trainer(1, TRAINING_TYPE_1, USER_5);
    public static final Trainer TRAINER_2 = new Trainer(2, TRAINING_TYPE_2, USER_6);
    public static final Trainer TRAINER_3 = new Trainer(3, TRAINING_TYPE_3, USER_7);
    public static final Trainer TRAINER_4 = new Trainer(4, TRAINING_TYPE_4, USER_8);

    public static final TrainerTo TRAINER_TO_1 = TrainerTo.builder()
            .id(1)
            .username(USER_5.getUsername())
            .firstName(USER_5.getFirstName())
            .lastName(USER_5.getLastName())
            .isActive(USER_5.isActive())
            .specialization(TRAINING_TYPE_1)
            .specializationId(TRAINING_TYPE_1_ID)
            .build();

    public static final TrainerTo TRAINER_TO_2 = TrainerTo.builder()
            .id(2)
            .username(USER_6.getUsername())
            .firstName(USER_6.getFirstName())
            .lastName(USER_6.getLastName())
            .isActive(USER_6.isActive())
            .specialization(TRAINING_TYPE_2)
            .specializationId(TRAINING_TYPE_1_ID + 1)
            .build();

    public static final TrainerTo TRAINER_TO_3 = TrainerTo.builder()
            .id(3)
            .username(USER_7.getUsername())
            .firstName(USER_7.getFirstName())
            .lastName(USER_7.getLastName())
            .isActive(USER_7.isActive())
            .specialization(TRAINING_TYPE_3)
            .specializationId(TRAINING_TYPE_1_ID + 2)
            .build();

    public static final TrainerTo TRAINER_TO_4 = TrainerTo.builder()
            .id(4)
            .username(USER_8.getUsername())
            .firstName(USER_8.getFirstName())
            .lastName(USER_8.getLastName())
            .isActive(USER_8.isActive())
            .specialization(TRAINING_TYPE_4)
            .specializationId(TRAINING_TYPE_1_ID + 3)
            .build();

    public static final List<TrainerTo> FREE_TRAINERS_FOR_TRAINEE_1 = List.of(TRAINER_TO_1, TRAINER_TO_3);
    public static final List<TrainerTo> FREE_TRAINERS_FOR_TRAINEE_2 = List.of(TRAINER_TO_1, TRAINER_TO_4);
    public static final List<TrainerTo> FREE_TRAINERS_FOR_TRAINEE_3 = List.of(TRAINER_TO_1, TRAINER_TO_3, TRAINER_TO_4);
    public static final List<TrainerTo> FREE_TRAINERS_FOR_TRAINEE_4 = List.of(TRAINER_TO_2, TRAINER_TO_3);

    public static final MatcherFactory.Matcher<Trainer> TRAINER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Trainer.class, "user", "specialization");
    public static final MatcherFactory.Matcher<TrainerTo> TRAINER_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(TrainerTo.class, "traineeList");

    public static final MatcherFactory.Matcher<TrainerTo> TRAINER_TO_MATCHER_WITH_TRAINEE_LIST =
            MatcherFactory.usingAssertions(TrainerTo.class,
                    (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("traineeList.trainerList").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    static {
        TRAINER_TO_1.setTraineeList(List.of(TRAINEE_TO_4));
        TRAINER_TO_2.setTraineeList(List.of(TRAINEE_TO_1, TRAINEE_TO_2, TRAINEE_TO_3));
        TRAINER_TO_3.setTraineeList(List.of(TRAINEE_TO_2));
        TRAINER_TO_4.setTraineeList(List.of(TRAINEE_TO_1, TRAINEE_TO_4));
    }

    public static Trainer getNewTrainer() {
        return new Trainer(null, TRAINING_TYPE_4, USER_9);
    }

    public static Trainer getUpdatedTrainer() {
        return new Trainer(1, TRAINING_TYPE_4, USER_5);
    }

    public static TrainerTo getNewTrainerTo() {
        return TrainerTo.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .specializationId(TRAINING_TYPE_4.getId())
                .isActive(true)
                .build();
    }

    public static TrainerTo getUpdatedTrainerTo() {
        User updatedUser = UserTestData.getUpdatedUser();
        Trainer updatedTrainer = getUpdatedTrainer();
        return TrainerTo.builder()
                .id(updatedTrainer.id())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .specialization(updatedTrainer.getSpecialization())
                .specializationId(updatedTrainer.getSpecialization().getId())
                .isActive(updatedUser.isActive())
                .username(USER_5.getUsername())
                .build();
    }

    public static void checkTrainerUserId(Trainer expected, Trainer actual) {
        Assertions.assertEquals(expected.getUser().getId(), actual.getUser().getId());
    }

    public static void checkTrainerSpecializationId(Trainer expected, Trainer actual) {
        Assertions.assertEquals(expected.getSpecialization().getId(), actual.getSpecialization().getId());
    }
}
