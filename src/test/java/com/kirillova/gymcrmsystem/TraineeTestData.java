package com.kirillova.gymcrmsystem;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.models.User;
import com.kirillova.gymcrmsystem.to.TraineeTo;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.util.List;

import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_TO_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_TO_2;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_TO_3;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_TO_4;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_1_USERNAME;
import static com.kirillova.gymcrmsystem.UserTestData.USER_2;
import static com.kirillova.gymcrmsystem.UserTestData.USER_3;
import static com.kirillova.gymcrmsystem.UserTestData.USER_4;
import static com.kirillova.gymcrmsystem.UserTestData.USER_9;
import static org.assertj.core.api.Assertions.assertThat;

public class TraineeTestData {
    public static final int TRAINEE_1_ID = 1;

    public static final Trainee TRAINEE_1 = new Trainee(1, LocalDate.of(1975, 6, 4), "some address", USER_1);
    public static final Trainee TRAINEE_2 = new Trainee(2, LocalDate.of(1976, 10, 23), "some address", USER_2);
    public static final Trainee TRAINEE_3 = new Trainee(3, LocalDate.of(1976, 9, 15), "some address", USER_3);
    public static final Trainee TRAINEE_4 = new Trainee(4, LocalDate.of(1964, 9, 2), "some address", USER_4);

    public static final TraineeTo TRAINEE_TO_1 = TraineeTo.builder()
            .id(1)
            .username(USER_1.getUsername())
            .firstName(USER_1.getFirstName())
            .lastName(USER_1.getLastName())
            .birthday(TRAINEE_1.getDateOfBirth())
            .address(TRAINEE_1.getAddress())
            .isActive(USER_1.isActive())
            .build();

    public static final TraineeTo TRAINEE_TO_2 = TraineeTo.builder()
            .id(2)
            .username(USER_2.getUsername())
            .firstName(USER_2.getFirstName())
            .lastName(USER_2.getLastName())
            .birthday(TRAINEE_2.getDateOfBirth())
            .address(TRAINEE_2.getAddress())
            .isActive(USER_2.isActive())
            .build();

    public static final TraineeTo TRAINEE_TO_3 = TraineeTo.builder()
            .id(3)
            .username(USER_3.getUsername())
            .firstName(USER_3.getFirstName())
            .lastName(USER_3.getLastName())
            .birthday(TRAINEE_3.getDateOfBirth())
            .address(TRAINEE_3.getAddress())
            .isActive(USER_3.isActive())
            .build();

    public static final TraineeTo TRAINEE_TO_4 = TraineeTo.builder()
            .id(4)
            .username(USER_4.getUsername())
            .firstName(USER_4.getFirstName())
            .lastName(USER_4.getLastName())
            .birthday(TRAINEE_4.getDateOfBirth())
            .address(TRAINEE_4.getAddress())
            .isActive(USER_4.isActive())
            .build();

    public static final MatcherFactory.Matcher<Trainee> TRAINEE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Trainee.class, "user");
    public static final MatcherFactory.Matcher<TraineeTo> TRAINEE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(TraineeTo.class, "trainerList");

    public static final MatcherFactory.Matcher<TraineeTo> TRAINEE_TO_MATCHER_WITH_TRAINER_LIST =
            MatcherFactory.usingAssertions(TraineeTo.class,
                    (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("trainerList.traineeList", "trainerList.specializationId").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    static {
        TRAINEE_TO_1.setTrainerList(List.of(TRAINER_TO_2, TRAINER_TO_4));
        TRAINEE_TO_2.setTrainerList(List.of(TRAINER_TO_2, TRAINER_TO_3));
        TRAINEE_TO_3.setTrainerList(List.of(TRAINER_TO_2));
        TRAINEE_TO_4.setTrainerList(List.of(TRAINER_TO_1, TRAINER_TO_4));
    }

    public static Trainee getNewTrainee() {
        return new Trainee(null, LocalDate.of(1969, 11, 4), "some new address", USER_9);
    }

    public static TraineeTo getNewTraineeTo() {
        return TraineeTo.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthday(LocalDate.of(1980, 8, 15))
                .address("Address")
                .isActive(true)
                .build();
    }

    public static Trainee getUpdatedTrainee() {
        return new Trainee(1, LocalDate.of(1970, 12, 1), "updated address", USER_1);
    }

    public static TraineeTo getUpdatedTraineeTo() {
        User updatedUser = UserTestData.getUpdatedUser();
        Trainee updatedTrainee = getUpdatedTrainee();
        return TraineeTo.builder()
                .id(updatedTrainee.id())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .birthday(updatedTrainee.getDateOfBirth())
                .address(updatedTrainee.getAddress())
                .isActive(updatedUser.isActive())
                .username(USER_1_USERNAME)
                .build();
    }

    public static void checkTraineeUserId(Trainee expected, Trainee actual) {
        Assertions.assertEquals(expected.getUser().getId(), actual.getUser().getId());
    }

}
