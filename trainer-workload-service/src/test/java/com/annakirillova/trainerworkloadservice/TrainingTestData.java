package com.annakirillova.trainerworkloadservice;

import com.annakirillova.trainerworkloadservice.dto.TrainingDto;
import com.annakirillova.trainerworkloadservice.model.Training;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.util.List;

import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_2;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_3;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_4;

public class TrainingTestData {
    public static final int TRAINING_1_ID = 1;

    public static final Training TRAINING_1 = new Training(1, TRAINER_4, LocalDate.of(2024, 1, 1), 60);
    public static final Training TRAINING_2 = new Training(2, TRAINER_2, LocalDate.of(2024, 1, 2), 60);
    public static final Training TRAINING_3 = new Training(3, TRAINER_2, LocalDate.of(2024, 1, 2), 60);
    public static final Training TRAINING_4 = new Training(4, TRAINER_3, LocalDate.of(2024, 1, 5), 60);
    public static final Training TRAINING_5 = new Training(5, TRAINER_2, LocalDate.of(2024, 2, 5), 60);
    public static final Training TRAINING_6 = new Training(6, TRAINER_4, LocalDate.of(2024, 1, 1), 60);
    public static final Training TRAINING_7 = new Training(7, TRAINER_1, LocalDate.of(2024, 1, 6), 60);
    public static final Training TRAINING_8 = new Training(8, TRAINER_3, LocalDate.of(2024, 1, 6), 60);

    public static final TrainingDto TRAINING_DTO_ADD = TrainingDto.builder()
            .username(TRAINER_1.getUsername())
            .firstName(TRAINER_1.getFirstName())
            .lastName(TRAINER_1.getLastName())
            .isActive(TRAINER_1.isActive())
            .date(TRAINING_5.getDate())
            .duration(TRAINING_5.getDuration())
            .actionType(TrainingDto.ACTION_TYPE_ADD)
            .build();

    public static final TrainingDto TRAINING_DTO_DELETE = TrainingDto.builder()
            .username(TRAINER_1.getUsername())
            .firstName(TRAINER_1.getFirstName())
            .lastName(TRAINER_1.getLastName())
            .isActive(TRAINER_1.isActive())
            .date(TRAINING_7.getDate())
            .duration(TRAINING_7.getDuration())
            .actionType(TrainingDto.ACTION_TYPE_DELETE)
            .build();

    public static final TrainingDto TRAINING_DTO_INVALID_ACTION_TYPE = TrainingDto.builder()
            .username(TRAINER_1.getUsername())
            .firstName(TRAINER_1.getFirstName())
            .lastName(TRAINER_1.getLastName())
            .isActive(TRAINER_1.isActive())
            .date(TRAINING_7.getDate())
            .duration(TRAINING_7.getDuration())
            .actionType("INVALID_ACTION_TYPE")
            .build();

    public static final List<Training> TRAINING_LIST_FOR_TRAINEE_2 = List.of(TRAINING_2, TRAINING_3, TRAINING_5);

    public static final MatcherFactory.Matcher<Training> TRAINING_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Training.class, "trainer");

    public static Training getNewTraining() {
        return new Training(null, TRAINER_3, LocalDate.of(2024, 3, 5), 60);
    }

    public static void checkTrainingTrainerId(Training expected, Training actual) {
        Assertions.assertEquals(expected.getTrainer().getId(), actual.getTrainer().getId());
    }
}
