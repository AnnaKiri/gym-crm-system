package com.annakirillova.crmsystem;

import com.annakirillova.crmsystem.dto.TraineeDto;
import com.annakirillova.crmsystem.models.Trainee;
import com.annakirillova.crmsystem.models.User;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.annakirillova.crmsystem.TrainerTestData.TRAINERS_FOR_TRAINEE_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_2;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_3;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_DTO_4;
import static com.annakirillova.crmsystem.UserTestData.USER_1;
import static com.annakirillova.crmsystem.UserTestData.USER_1_USERNAME;
import static com.annakirillova.crmsystem.UserTestData.USER_2;
import static com.annakirillova.crmsystem.UserTestData.USER_3;
import static com.annakirillova.crmsystem.UserTestData.USER_4;
import static com.annakirillova.crmsystem.UserTestData.USER_9;
import static org.assertj.core.api.Assertions.assertThat;

public class TraineeTestData {
    public static final int TRAINEE_1_ID = 1;

    public static final Trainee TRAINEE_1 = new Trainee(1, LocalDate.of(1975, 6, 4), "some address", USER_1);
    public static final Trainee TRAINEE_2 = new Trainee(2, LocalDate.of(1976, 10, 23), "some address", USER_2);
    public static final Trainee TRAINEE_3 = new Trainee(3, LocalDate.of(1976, 9, 15), "some address", USER_3);
    public static final Trainee TRAINEE_4 = new Trainee(4, LocalDate.of(1964, 9, 2), "some address", USER_4);
    public static final List<Trainee> TRAINEES_FOR_TRAINER_1 = List.of(TRAINEE_4);

    public static final TraineeDto TRAINEE_DTO_1 = TraineeDto.builder()
            .id(1)
            .username(USER_1.getUsername())
            .firstName(USER_1.getFirstName())
            .lastName(USER_1.getLastName())
            .birthday(TRAINEE_1.getDateOfBirth())
            .address(TRAINEE_1.getAddress())
            .isActive(USER_1.isActive())
            .build();

    public static final TraineeDto TRAINEE_DTO_1_WITH_TRAINER_LIST = new TraineeDto(TRAINEE_DTO_1);

    public static final TraineeDto TRAINEE_DTO_2 = TraineeDto.builder()
            .id(2)
            .username(USER_2.getUsername())
            .firstName(USER_2.getFirstName())
            .lastName(USER_2.getLastName())
            .birthday(TRAINEE_2.getDateOfBirth())
            .address(TRAINEE_2.getAddress())
            .isActive(USER_2.isActive())
            .build();

    public static final TraineeDto TRAINEE_DTO_2_WITH_TRAINER_LIST = new TraineeDto(TRAINEE_DTO_2);

    public static final TraineeDto TRAINEE_DTO_3 = TraineeDto.builder()
            .id(3)
            .username(USER_3.getUsername())
            .firstName(USER_3.getFirstName())
            .lastName(USER_3.getLastName())
            .birthday(TRAINEE_3.getDateOfBirth())
            .address(TRAINEE_3.getAddress())
            .isActive(USER_3.isActive())
            .build();

    public static final TraineeDto TRAINEE_DTO_3_WITH_TRAINER_LIST = new TraineeDto(TRAINEE_DTO_3);

    public static final TraineeDto TRAINEE_DTO_4 = TraineeDto.builder()
            .id(4)
            .username(USER_4.getUsername())
            .firstName(USER_4.getFirstName())
            .lastName(USER_4.getLastName())
            .birthday(TRAINEE_4.getDateOfBirth())
            .address(TRAINEE_4.getAddress())
            .isActive(USER_4.isActive())
            .build();

    public static final TraineeDto TRAINEE_DTO_4_WITH_TRAINER_LIST = new TraineeDto(TRAINEE_DTO_4);

    public static final MatcherFactory.Matcher<Trainee> TRAINEE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Trainee.class, "user", "trainerList");
    public static final MatcherFactory.Matcher<TraineeDto> TRAINEE_DTO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(TraineeDto.class, "trainerList");

    public static final MatcherFactory.Matcher<TraineeDto> TRAINEE_DTO_MATCHER_WITH_TRAINER_LIST =
            MatcherFactory.usingAssertions(TraineeDto.class,
                    (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("trainerList.traineeList", "trainerList.specializationId").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    static {
        TRAINEE_1.setTrainerList(new ArrayList<>(TRAINERS_FOR_TRAINEE_1));
        TRAINEE_DTO_1_WITH_TRAINER_LIST.setTrainerList(List.of(TRAINER_DTO_2, TRAINER_DTO_4));
        TRAINEE_DTO_2_WITH_TRAINER_LIST.setTrainerList(List.of(TRAINER_DTO_2, TRAINER_DTO_3));
        TRAINEE_DTO_3_WITH_TRAINER_LIST.setTrainerList(List.of(TRAINER_DTO_2));
        TRAINEE_DTO_4_WITH_TRAINER_LIST.setTrainerList(List.of(TRAINER_DTO_1, TRAINER_DTO_4));
    }

    public static Trainee getNewTrainee() {
        return new Trainee(null, LocalDate.of(1969, 11, 4), "some new address", USER_9);
    }

    public static TraineeDto getNewTraineeDto() {
        return TraineeDto.builder()
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

    public static TraineeDto getUpdatedTraineeDto() {
        User updatedUser = UserTestData.getUpdatedUser();
        Trainee updatedTrainee = getUpdatedTrainee();
        return TraineeDto.builder()
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
