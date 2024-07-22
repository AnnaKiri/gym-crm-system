package com.kirillova.gymcrmsystem;

import com.kirillova.gymcrmsystem.models.Training;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;

import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_1;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_2;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_3;
import static com.kirillova.gymcrmsystem.TraineeTestData.TRAINEE_4;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_1;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_2;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_3;
import static com.kirillova.gymcrmsystem.TrainerTestData.TRAINER_4;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_1;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_2;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_3;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_4;

public class TrainingTestData {
    public static final int TRAINING_1_ID = 1;

    public static final Training TRAINING_1 = new Training(1, TRAINEE_1, TRAINER_4, "Stretching", TRAINING_TYPE_4, LocalDate.of(2024, 1, 1), 60);
    public static final Training TRAINING_2 = new Training(2, TRAINEE_1, TRAINER_2, "Aerobic", TRAINING_TYPE_2, LocalDate.of(2024, 1, 2), 60);
    public static final Training TRAINING_3 = new Training(3, TRAINEE_2, TRAINER_2, "Aerobic", TRAINING_TYPE_2, LocalDate.of(2024, 1, 2), 60);
    public static final Training TRAINING_4 = new Training(4, TRAINEE_2, TRAINER_3, "Yoga", TRAINING_TYPE_3, LocalDate.of(2024, 1, 5), 60);
    public static final Training TRAINING_5 = new Training(5, TRAINEE_3, TRAINER_2, "Strength", TRAINING_TYPE_1, LocalDate.of(2024, 1, 5), 60);
    public static final Training TRAINING_6 = new Training(6, TRAINEE_4, TRAINER_4, "Stretching", TRAINING_TYPE_4, LocalDate.of(2024, 1, 1), 60);
    public static final Training TRAINING_7 = new Training(7, TRAINEE_4, TRAINER_1, "Strength", TRAINING_TYPE_1, LocalDate.of(2024, 1, 6), 60);
    public static final Training TRAINING_8 = new Training(8, TRAINEE_2, TRAINER_3, "Yoga", TRAINING_TYPE_3, LocalDate.of(2024, 1, 5), 60);

    public static final MatcherFactory.Matcher<Training> TRAINING_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Training.class, "trainee", "trainer", "type");

    public static Training getNewTraining() {
        return new Training(null, TRAINEE_3, TRAINER_3, "Yoga", TRAINING_TYPE_3, LocalDate.of(2024, 1, 5), 60);
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
}
