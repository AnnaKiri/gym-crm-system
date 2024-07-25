package com.kirillova.gymcrmsystem;

import com.kirillova.gymcrmsystem.models.Trainee;
import com.kirillova.gymcrmsystem.to.TraineeTo;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;

import static com.kirillova.gymcrmsystem.UserTestData.USER_1;
import static com.kirillova.gymcrmsystem.UserTestData.USER_2;
import static com.kirillova.gymcrmsystem.UserTestData.USER_3;
import static com.kirillova.gymcrmsystem.UserTestData.USER_4;
import static com.kirillova.gymcrmsystem.UserTestData.USER_9;

public class TraineeTestData {
    public static final int TRAINEE_1_ID = 1;

    public static final Trainee TRAINEE_1 = new Trainee(1, LocalDate.of(1975, 6, 4), "some address", USER_1);
    public static final Trainee TRAINEE_2 = new Trainee(2, LocalDate.of(1976, 10, 23), "some address", USER_2);
    public static final Trainee TRAINEE_3 = new Trainee(3, LocalDate.of(1976, 9, 15), "some address", USER_3);
    public static final Trainee TRAINEE_4 = new Trainee(4, LocalDate.of(1964, 9, 2), "some address", USER_4);

    public static final MatcherFactory.Matcher<Trainee> TRAINEE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Trainee.class, "trainerList", "user");

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

    public static void checkTraineeUserId(Trainee expected, Trainee actual) {
        Assertions.assertEquals(expected.getUser().getId(), actual.getUser().getId());
    }

}
