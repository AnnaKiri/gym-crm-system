package com.annakirillova.crmsystem;

import com.annakirillova.common.dto.TrainingDto;
import com.annakirillova.common.dto.TrainingTypeDto;
import com.annakirillova.crmsystem.models.Training;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_1;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_2;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_3;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_4;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_2;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_3;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_4;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_1;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_2;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_3;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_4;
import static com.annakirillova.crmsystem.UserTestData.USER_1;
import static com.annakirillova.crmsystem.UserTestData.USER_2;
import static com.annakirillova.crmsystem.UserTestData.USER_3;
import static com.annakirillova.crmsystem.UserTestData.USER_4;
import static com.annakirillova.crmsystem.UserTestData.USER_5;
import static com.annakirillova.crmsystem.UserTestData.USER_6;
import static com.annakirillova.crmsystem.UserTestData.USER_7;
import static com.annakirillova.crmsystem.UserTestData.USER_8;

public class TrainingTestData {
    public static final int TRAINING_1_ID = 1;

    public static final Training TRAINING_1 = new Training(1, TRAINEE_1, TRAINER_4, "Stretching", TRAINING_TYPE_4, LocalDate.of(2024, 1, 1), 60);
    public static final Training TRAINING_2 = new Training(2, TRAINEE_1, TRAINER_2, "Aerobic", TRAINING_TYPE_2, LocalDate.of(2024, 1, 2), 60);
    public static final Training TRAINING_3 = new Training(3, TRAINEE_2, TRAINER_2, "Aerobic", TRAINING_TYPE_2, LocalDate.of(2024, 1, 2), 60);
    public static final Training TRAINING_4 = new Training(4, TRAINEE_2, TRAINER_3, "Yoga", TRAINING_TYPE_3, LocalDate.of(2024, 1, 5), 60);
    public static final Training TRAINING_5 = new Training(5, TRAINEE_3, TRAINER_2, "Strength", TRAINING_TYPE_1, LocalDate.of(2024, 1, 5), 60);
    public static final Training TRAINING_6 = new Training(6, TRAINEE_4, TRAINER_4, "Stretching", TRAINING_TYPE_4, LocalDate.of(2024, 1, 1), 60);
    public static final Training TRAINING_7 = new Training(7, TRAINEE_4, TRAINER_1, "Strength", TRAINING_TYPE_1, LocalDate.of(2024, 1, 6), 60);

    public static final TrainingDto TRAINING_DTO_1 = TrainingDto.builder()
            .id(1)
            .name(TRAINING_1.getName())
            .type(TrainingTypeDto.builder().name(TRAINING_1.getType().getName()).build())
            .typeId(TRAINING_1.getType().getId())
            .date(TRAINING_1.getDate())
            .duration(TRAINING_1.getDuration())
            .traineeUsername(TRAINEE_1.getUser().getUsername())
            .trainerUsername(TRAINER_4.getUser().getUsername())
            .build();

    public static final TrainingDto TRAINING_DTO_2 = TrainingDto.builder()
            .id(2)
            .name(TRAINING_2.getName())
            .type(TrainingTypeDto.builder().name(TRAINING_2.getType().getName()).build())
            .typeId(TRAINING_2.getType().getId())
            .date(TRAINING_2.getDate())
            .duration(TRAINING_2.getDuration())
            .traineeUsername(TRAINEE_1.getUser().getUsername())
            .trainerUsername(TRAINER_2.getUser().getUsername())
            .build();

    public static final TrainingDto TRAINING_DTO_3 = TrainingDto.builder()
            .id(3)
            .name(TRAINING_3.getName())
            .type(TrainingTypeDto.builder().name(TRAINING_3.getType().getName()).build())
            .typeId(TRAINING_3.getType().getId())
            .date(TRAINING_3.getDate())
            .duration(TRAINING_3.getDuration())
            .traineeUsername(TRAINEE_2.getUser().getUsername())
            .trainerUsername(TRAINER_2.getUser().getUsername())
            .build();

    public static final TrainingDto TRAINING_DTO_4 = TrainingDto.builder()
            .id(4)
            .name(TRAINING_4.getName())
            .type(TrainingTypeDto.builder().name(TRAINING_4.getType().getName()).build())
            .typeId(TRAINING_4.getType().getId())
            .date(TRAINING_4.getDate())
            .duration(TRAINING_4.getDuration())
            .traineeUsername(TRAINEE_2.getUser().getUsername())
            .trainerUsername(TRAINER_3.getUser().getUsername())
            .build();

    public static final TrainingDto TRAINING_DTO_5 = TrainingDto.builder()
            .id(5)
            .name(TRAINING_5.getName())
            .type(TrainingTypeDto.builder().name(TRAINING_5.getType().getName()).build())
            .typeId(TRAINING_5.getType().getId())
            .date(TRAINING_5.getDate())
            .duration(TRAINING_5.getDuration())
            .traineeUsername(TRAINEE_3.getUser().getUsername())
            .trainerUsername(TRAINER_2.getUser().getUsername())
            .build();

    public static final TrainingDto TRAINING_DTO_6 = TrainingDto.builder()
            .id(6)
            .name(TRAINING_6.getName())
            .type(TrainingTypeDto.builder().name(TRAINING_6.getType().getName()).build())
            .typeId(TRAINING_6.getType().getId())
            .date(TRAINING_6.getDate())
            .duration(TRAINING_6.getDuration())
            .traineeUsername(TRAINEE_4.getUser().getUsername())
            .trainerUsername(TRAINER_4.getUser().getUsername())
            .build();

    public static final TrainingDto TRAINING_DTO_7 = TrainingDto.builder()
            .id(7)
            .name(TRAINING_7.getName())
            .type(TrainingTypeDto.builder().name(TRAINING_7.getType().getName()).build())
            .typeId(TRAINING_7.getType().getId())
            .date(TRAINING_7.getDate())
            .duration(TRAINING_7.getDuration())
            .traineeUsername(TRAINEE_4.getUser().getUsername())
            .trainerUsername(TRAINER_1.getUser().getUsername())
            .build();

    public static final List<TrainingDto> TRAINING_DTO_LIST_FOR_TRAINEE_1 = List.of(TRAINING_DTO_1, TRAINING_DTO_2);
    public static final List<TrainingDto> TRAINING_DTO_LIST_FOR_TRAINEE_2 = List.of(TRAINING_DTO_3, TRAINING_DTO_4);
    public static final List<TrainingDto> TRAINING_DTO_LIST_FOR_TRAINEE_3 = List.of(TRAINING_DTO_5);
    public static final List<TrainingDto> TRAINING_DTO_LIST_FOR_TRAINEE_4 = List.of(TRAINING_DTO_6, TRAINING_DTO_7);

    public static final List<TrainingDto> TRAINING_DTO_LIST_FOR_TRAINER_1 = List.of(TRAINING_DTO_7);
    public static final List<TrainingDto> TRAINING_DTO_LIST_FOR_TRAINER_2 = List.of(TRAINING_DTO_2, TRAINING_DTO_3, TRAINING_DTO_5);
    public static final List<TrainingDto> TRAINING_DTO_LIST_FOR_TRAINER_3 = List.of(TRAINING_DTO_4);
    public static final List<TrainingDto> TRAINING_DTO_LIST_FOR_TRAINER_4 = List.of(TRAINING_DTO_1, TRAINING_DTO_6);

    public static final MatcherFactory.Matcher<Training> TRAINING_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Training.class, "trainee", "trainer", "type");
    public static final MatcherFactory.Matcher<TrainingDto> TRAINING_DTO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(TrainingDto.class, "typeId");

    public static Training getNewTraining() {
        return new Training(null, TRAINEE_3, TRAINER_3, "Yoga", TRAINING_TYPE_3, LocalDate.of(2024, 1, 5), 60);
    }

    public static TrainingDto getNewTrainingDto() {
        return TrainingDto.builder()
                .name("Yoga")
                .type(TrainingTypeDto.builder().name(TRAINING_TYPE_3.getName()).build())
                .typeId(TRAINING_TYPE_3.getId())
                .date(LocalDate.of(2024, 1, 5))
                .duration(60)
                .traineeUsername(TRAINEE_3.getUser().getUsername())
                .trainerUsername(TRAINER_3.getUser().getUsername())
                .build();
    }

    public static void checkTrainingTraineeId(Training expected, Training actual) {
        Assertions.assertEquals(expected.getTrainee().getId(), actual.getTrainee().getId());
    }

    public static void checkTrainingTrainerId(Training expected, Training actual) {
        Assertions.assertEquals(expected.getTrainer().getId(), actual.getTrainer().getId());
    }

    public static void checkTrainingTypeId(Training expected, Training actual) {
        Assertions.assertEquals(expected.getType().getId(), actual.getType().getId());
    }

    public static final Map<String, List<TrainingDto>> USERNAMES_TO_TRAINEE_TRAININGS_LIST = Map.of(
            USER_1.getUsername(), TRAINING_DTO_LIST_FOR_TRAINEE_1,
            USER_2.getUsername(), TRAINING_DTO_LIST_FOR_TRAINEE_2,
            USER_3.getUsername(), TRAINING_DTO_LIST_FOR_TRAINEE_3,
            USER_4.getUsername(), TRAINING_DTO_LIST_FOR_TRAINEE_4,
            USER_5.getUsername(), TRAINING_DTO_LIST_FOR_TRAINER_1,
            USER_6.getUsername(), TRAINING_DTO_LIST_FOR_TRAINER_2,
            USER_7.getUsername(), TRAINING_DTO_LIST_FOR_TRAINER_3,
            USER_8.getUsername(), TRAINING_DTO_LIST_FOR_TRAINER_4
    );

    public static final Map<Integer, TrainingDto> TRAINING_ID_TO_TRAINING = new HashMap<>(Map.of(
            TRAINING_DTO_1.getId(), TRAINING_DTO_1,
            TRAINING_DTO_2.getId(), TRAINING_DTO_2,
            TRAINING_DTO_3.getId(), TRAINING_DTO_3,
            TRAINING_DTO_4.getId(), TRAINING_DTO_4,
            TRAINING_DTO_5.getId(), TRAINING_DTO_5,
            TRAINING_DTO_6.getId(), TRAINING_DTO_6,
            TRAINING_DTO_7.getId(), TRAINING_DTO_7
    ));
}
