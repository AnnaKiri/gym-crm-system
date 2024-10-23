package com.annakirillova.crmsystem.repository;

import com.annakirillova.crmsystem.exception.NotFoundException;
import com.annakirillova.crmsystem.models.Training;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_1;
import static com.annakirillova.crmsystem.TrainerTestData.TRAINER_4;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_1;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_1_ID;
import static com.annakirillova.crmsystem.TrainingTestData.TRAINING_MATCHER;
import static com.annakirillova.crmsystem.TrainingTestData.checkTrainingTraineeId;
import static com.annakirillova.crmsystem.TrainingTestData.checkTrainingTrainerId;
import static com.annakirillova.crmsystem.TrainingTestData.checkTrainingTypeId;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_2;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_4;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingRepositoryUnitTest extends BaseRepositoryUnitTest {

    @Autowired
    private TrainingRepository trainingRepository;

    @Test
    void getFullTrainingById() {
        Training retrievedTraining = trainingRepository.getFullTrainingById(TRAINING_1_ID).orElse(null);
        TRAINING_MATCHER.assertMatch(retrievedTraining, TRAINING_1);
        checkTrainingTraineeId(TRAINING_1, retrievedTraining);
        checkTrainingTrainerId(TRAINING_1, retrievedTraining);
        checkTrainingTypeId(TRAINING_1, retrievedTraining);
    }

    @Test
    void getTrainingIfExistsWithWrongId() {
        Training retrievedTraining = trainingRepository.getTrainingIfExists(TRAINING_1_ID);
        TRAINING_MATCHER.assertMatch(retrievedTraining, TRAINING_1);
    }

    @Test
    void getTrainingIfExistsNotFound() {
        Assertions.assertThrows(NotFoundException.class,
                () -> trainingRepository.getTrainingIfExists(TRAINING_1_ID + 10));
    }

    @Test
    void testHasTraineeUsername() {
        Specification<Training> spec = TrainingSpecifications.hasTraineeUsername(TRAINEE_1.getUser().getUsername());
        List<Training> results = trainingRepository.findAll(spec);

        assertEquals(2, results.size());
        assertEquals(TRAINEE_1.getUser().getUsername(), results.get(0).getTrainee().getUser().getUsername());
    }

    @Test
    void testHasTrainerUsername() {
        Specification<Training> spec = TrainingSpecifications.hasTrainerUsername(TRAINER_4.getUser().getUsername());
        List<Training> results = trainingRepository.findAll(spec);

        assertEquals(2, results.size());
        assertEquals(TRAINER_4.getUser().getUsername(), results.get(0).getTrainer().getUser().getUsername());
    }

    @Test
    void testIsBetweenDates() {
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        LocalDate toDate = LocalDate.of(2024, 1, 5);
        Specification<Training> spec = TrainingSpecifications.isBetweenDates(fromDate, toDate);
        List<Training> results = trainingRepository.findAll(spec);

        assertEquals(6, results.size());
    }

    @Test
    void testIsAfterDate() {
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        Specification<Training> spec = TrainingSpecifications.isBetweenDates(fromDate, null);
        List<Training> results = trainingRepository.findAll(spec);

        assertEquals(7, results.size());
    }

    @Test
    void testIsBeforeDate() {
        LocalDate toDate = LocalDate.of(2024, 1, 2);
        Specification<Training> spec = TrainingSpecifications.isBetweenDates(null, toDate);
        List<Training> results = trainingRepository.findAll(spec);

        assertEquals(4, results.size());
    }

    @Test
    void testHasTrainingType() {
        Specification<Training> spec = TrainingSpecifications.hasTrainingType(TRAINING_TYPE_2.getName());
        List<Training> results = trainingRepository.findAll(spec);

        assertEquals(2, results.size());
    }

    @Test
    void testHasTrainerFirstName() {
        Specification<Training> spec = TrainingSpecifications.hasTrainerFirstName(TRAINER_4.getUser().getFirstName());
        List<Training> results = trainingRepository.findAll(spec);

        assertEquals(2, results.size());
        assertEquals(TRAINER_4.getUser().getFirstName(), results.get(0).getTrainer().getUser().getFirstName());
    }

    @Test
    void testHasTrainerLastName() {
        Specification<Training> spec = TrainingSpecifications.hasTrainerLastName(TRAINER_4.getUser().getLastName());
        List<Training> results = trainingRepository.findAll(spec);

        assertEquals(2, results.size());
        assertEquals(TRAINER_4.getUser().getLastName(), results.get(0).getTrainer().getUser().getLastName());
    }

    @Test
    void testGetTraineeTrainings() {
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        LocalDate toDate = LocalDate.of(2024, 1, 5);
        Specification<Training> spec = TrainingSpecifications.getTraineeTrainings(TRAINEE_1.getUser().getUsername(),
                fromDate, toDate, TRAINING_TYPE_4.getName(), TRAINER_4.getUser().getFirstName(),
                TRAINER_4.getUser().getLastName());
        List<Training> results = trainingRepository.findAll(spec);

        assertEquals(1, results.size());
    }

    @Test
    void testGetTrainerTrainings() {
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        LocalDate toDate = LocalDate.of(2024, 1, 5);
        Specification<Training> spec = TrainingSpecifications.getTrainerTrainings(TRAINER_4.getUser().getUsername(),
                fromDate, toDate, TRAINEE_1.getUser().getFirstName(),
                TRAINEE_1.getUser().getLastName());
        List<Training> results = trainingRepository.findAll(spec);

        assertEquals(1, results.size());
    }

    @Test
    void testFindAllWithDetails() {
        Specification<Training> spec = TrainingSpecifications.hasTraineeUsername(TRAINEE_1.getUser().getUsername());
        List<Training> results = trainingRepository.findAllWithDetails(spec);

        assertEquals(2, results.size());
        assertEquals(TRAINEE_1.getUser().getUsername(), results.get(0).getTrainee().getUser().getUsername());

        assertEquals(TRAINING_TYPE_2.getName(), results.get(1).getType().getName());
        assertEquals(TRAINER_4.getUser().getFirstName(), results.get(0).getTrainer().getUser().getFirstName());
    }
}
