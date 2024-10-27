package com.annakirillova.crmsystem;

import com.annakirillova.common.dto.TrainerDto;
import com.annakirillova.common.dto.TrainingTypeDto;
import com.annakirillova.crmsystem.models.Trainer;
import com.annakirillova.crmsystem.models.User;
import com.annakirillova.crmsystem.util.JsonUtil;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.annakirillova.crmsystem.TraineeTestData.TRAINEES_FOR_TRAINER_1;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_DTO_1;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_DTO_2;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_DTO_3;
import static com.annakirillova.crmsystem.TraineeTestData.TRAINEE_DTO_4;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_1;
import static com.annakirillova.crmsystem.TrainingTypeTestData.TRAINING_TYPE_1_ID;
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
import static com.annakirillova.crmsystem.UserTestData.USER_9;
import static org.assertj.core.api.Assertions.assertThat;

public class TrainerTestData {
    public static final int TRAINER_1_ID = 1;

    public static final Trainer TRAINER_1 = new Trainer(1, TRAINING_TYPE_1, USER_5);
    public static final Trainer TRAINER_2 = new Trainer(2, TRAINING_TYPE_2, USER_6);
    public static final Trainer TRAINER_3 = new Trainer(3, TRAINING_TYPE_3, USER_7);
    public static final Trainer TRAINER_4 = new Trainer(4, TRAINING_TYPE_4, USER_8);
    public static final List<Trainer> TRAINERS_FOR_TRAINEE_1 = List.of(TRAINER_2, TRAINER_4);

    public static final TrainerDto TRAINER_DTO_1 = TrainerDto.builder()
            .id(1)
            .username(USER_5.getUsername())
            .firstName(USER_5.getFirstName())
            .lastName(USER_5.getLastName())
            .isActive(USER_5.isActive())
            .specialization(TrainingTypeDto.builder().name(TRAINING_TYPE_1.getName()).build())
            .specializationId(TRAINING_TYPE_1_ID)
            .build();
    public static final TrainerDto TRAINER_DTO_1_WITH_TRAINEE_LIST = new TrainerDto(TRAINER_DTO_1);

    public static final TrainerDto TRAINER_DTO_2 = TrainerDto.builder()
            .id(2)
            .username(USER_6.getUsername())
            .firstName(USER_6.getFirstName())
            .lastName(USER_6.getLastName())
            .isActive(USER_6.isActive())
            .specialization(TrainingTypeDto.builder().name(TRAINING_TYPE_2.getName()).build())
            .specializationId(TRAINING_TYPE_1_ID + 1)
            .build();

    public static final TrainerDto TRAINER_DTO_2_WITH_TRAINEE_LIST = new TrainerDto(TRAINER_DTO_2);

    public static final TrainerDto TRAINER_DTO_3 = TrainerDto.builder()
            .id(3)
            .username(USER_7.getUsername())
            .firstName(USER_7.getFirstName())
            .lastName(USER_7.getLastName())
            .isActive(USER_7.isActive())
            .specialization(TrainingTypeDto.builder().name(TRAINING_TYPE_3.getName()).build())
            .specializationId(TRAINING_TYPE_1_ID + 2)
            .build();

    public static final TrainerDto TRAINER_DTO_3_WITH_TRAINEE_LIST = new TrainerDto(TRAINER_DTO_3);

    public static final TrainerDto TRAINER_DTO_4 = TrainerDto.builder()
            .id(4)
            .username(USER_8.getUsername())
            .firstName(USER_8.getFirstName())
            .lastName(USER_8.getLastName())
            .isActive(USER_8.isActive())
            .specialization(TrainingTypeDto.builder().name(TRAINING_TYPE_4.getName()).build())
            .specializationId(TRAINING_TYPE_1_ID + 3)
            .build();

    public static final TrainerDto TRAINER_DTO_4_WITH_TRAINEE_LIST = new TrainerDto(TRAINER_DTO_4);

    public static final List<TrainerDto> FREE_TRAINERS_FOR_TRAINEE_1 = List.of(TRAINER_DTO_1, TRAINER_DTO_3);
    public static final List<TrainerDto> FREE_TRAINERS_FOR_TRAINEE_2 = List.of(TRAINER_DTO_1, TRAINER_DTO_4);
    public static final List<TrainerDto> FREE_TRAINERS_FOR_TRAINEE_3 = List.of(TRAINER_DTO_1, TRAINER_DTO_3, TRAINER_DTO_4);
    public static final List<TrainerDto> FREE_TRAINERS_FOR_TRAINEE_4 = List.of(TRAINER_DTO_2, TRAINER_DTO_3);

    public static final MatcherFactory.Matcher<Trainer> TRAINER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Trainer.class, "user", "specialization", "traineeList");
    public static final MatcherFactory.Matcher<TrainerDto> TRAINER_DTO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(TrainerDto.class, "id", "traineeList", "specializationId");

    public static final MatcherFactory.Matcher<TrainerDto> TRAINER_DTO_MATCHER_WITH_TRAINEE_LIST =
            MatcherFactory.usingAssertions(TrainerDto.class,
                    (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("id", "traineeList.id", "traineeList.trainerList", "specializationId").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    static {
        TRAINER_1.setTraineeList(new ArrayList<>(TRAINEES_FOR_TRAINER_1));
        TRAINER_DTO_1_WITH_TRAINEE_LIST.setTraineeList(List.of(TRAINEE_DTO_4));
        TRAINER_DTO_2_WITH_TRAINEE_LIST.setTraineeList(List.of(TRAINEE_DTO_1, TRAINEE_DTO_2, TRAINEE_DTO_3));
        TRAINER_DTO_3_WITH_TRAINEE_LIST.setTraineeList(List.of(TRAINEE_DTO_2));
        TRAINER_DTO_4_WITH_TRAINEE_LIST.setTraineeList(List.of(TRAINEE_DTO_1, TRAINEE_DTO_4));
    }

    public static Trainer getNewTrainer() {
        return new Trainer(null, TRAINING_TYPE_4, USER_9);
    }

    public static Trainer getUpdatedTrainer() {
        return new Trainer(1, TRAINING_TYPE_4, USER_5);
    }

    public static TrainerDto getNewTrainerDto() {
        return TrainerDto.builder()
                .firstName("TrainerFirstname")
                .lastName("TrainerLastName")
                .specializationId(TRAINING_TYPE_4.getId())
                .isActive(true)
                .build();
    }

    public static TrainerDto getUpdatedTrainerDto() {
        User updatedUser = UserTestData.getUpdatedUser();
        Trainer updatedTrainer = getUpdatedTrainer();
        return TrainerDto.builder()
                .id(updatedTrainer.id())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .specialization(TrainingTypeDto.builder().name(updatedTrainer.getSpecialization().getName()).build())
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

    public static String jsonWithSpecializationId(TrainerDto trainerTo, int specializationId) {
        return JsonUtil.writeAdditionProps(trainerTo, "specializationId", specializationId);
    }

    public static final Map<String, TrainerDto> USERNAMES_TO_TRAINER_DTO = new HashMap<>(Map.of(
            USER_5.getUsername(), TRAINER_DTO_1_WITH_TRAINEE_LIST,
            USER_6.getUsername(), TRAINER_DTO_2_WITH_TRAINEE_LIST,
            USER_7.getUsername(), TRAINER_DTO_3_WITH_TRAINEE_LIST,
            USER_8.getUsername(), TRAINER_DTO_4_WITH_TRAINEE_LIST
    ));

    public static final Map<String, List<TrainerDto>> USERNAMES_TO_FREE_TRAINERS_LIST = Map.of(
            USER_1.getUsername(), FREE_TRAINERS_FOR_TRAINEE_1,
            USER_2.getUsername(), FREE_TRAINERS_FOR_TRAINEE_2,
            USER_3.getUsername(), FREE_TRAINERS_FOR_TRAINEE_3,
            USER_4.getUsername(), FREE_TRAINERS_FOR_TRAINEE_4
    );
}
