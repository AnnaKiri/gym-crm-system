package com.annakirillova.trainerworkloadservice;

import com.annakirillova.common.dto.TrainerSummaryDto;
import com.annakirillova.trainerworkloadservice.model.TrainerSummaryMongoDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TestData {
    public static final int TRAINER_1_ID = 1;
    public static final String TRAINEE_1_USERNAME = "angelina.jolie";
    public static final String TRAINER_1_USERNAME = "tom.cruise";
    public static final String TRAINER_2_USERNAME = "brad.pitt";
    public static final String NOT_FOUND_USERNAME = "not.found";

    public static final TrainerSummaryDto.Summary SUMMARY_1 = new TrainerSummaryDto.Summary(2024, 1, 60); // TRAINER_4
    public static final TrainerSummaryDto.Summary SUMMARY_2 = new TrainerSummaryDto.Summary(2024, 2, 60); // TRAINER_4
    public static final TrainerSummaryDto.Summary SUMMARY_3 = new TrainerSummaryDto.Summary(2024, 1, 120); // TRAINER_2
    public static final TrainerSummaryDto.Summary SUMMARY_4 = new TrainerSummaryDto.Summary(2024, 2, 60); // TRAINER_2
    public static final TrainerSummaryDto.Summary SUMMARY_5 = new TrainerSummaryDto.Summary(2024, 1, 120); // TRAINER_3
    public static final TrainerSummaryDto.Summary SUMMARY_6 = new TrainerSummaryDto.Summary(2024, 1, 60); // TRAINER_1
    public static final TrainerSummaryDto.Summary SUMMARY_7 = new TrainerSummaryDto.Summary(2024, 2, 60); // TRAINER_1

    public static final List<TrainerSummaryDto.Summary> SUMMARY_LIST_FOR_TRAINER_1 = List.of(SUMMARY_6, SUMMARY_7);
    public static final List<TrainerSummaryDto.Summary> SUMMARY_LIST_FOR_TRAINER_2 = List.of(SUMMARY_3, SUMMARY_4);

    public static final TrainerSummaryMongoDb TRAINER_SUMMARY_1 = new TrainerSummaryMongoDb("tom.cruise", "Tom", "Cruise", "true", new ArrayList<>(SUMMARY_LIST_FOR_TRAINER_1));
    public static final TrainerSummaryMongoDb TRAINER_SUMMARY_2 = new TrainerSummaryMongoDb("brad.pitt", "Brad", "Pitt", "true", SUMMARY_LIST_FOR_TRAINER_2);
    public static final TrainerSummaryMongoDb TRAINER_SUMMARY_3 = new TrainerSummaryMongoDb("jennifer.aniston", "Jennifer", "Aniston", "true", new ArrayList<>());
    public static final TrainerSummaryMongoDb TRAINER_SUMMARY_4 = new TrainerSummaryMongoDb("sandra.bullock", "Sandra", "Bullock", "true", new ArrayList<>());

    public static final Map<String, TrainerSummaryMongoDb> USERNAME_SUMMARY = new HashMap<>(Map.of(
            "tom.cruise", TRAINER_SUMMARY_1,
            "brad.pitt", TRAINER_SUMMARY_2,
            "jennifer.aniston", TRAINER_SUMMARY_3,
            "sandra.bullock", TRAINER_SUMMARY_4
    ));

    public static final Map<String, String> USERS_PASSWORDS = new HashMap<>(Map.of(
            "angelina.jolie", "password1",
            "ryan.reynolds", "password2",
            "tom.hardy", "password3",
            "keanu.reeves", "password4",
            "tom.cruise", "password5",
            "brad.pitt", "password6",
            "jennifer.aniston", "password7",
            "sandra.bullock", "password8",
            "matthew.mcconaughey", "password9"
    ));

    public static final MatcherFactory.Matcher<TrainerSummaryDto> TRAINER_MATCHER_WITH_SUMMARY_LIST =
            MatcherFactory.usingAssertions(TrainerSummaryDto.class,
                    (a, e) -> assertThat(a).usingRecursiveComparison().isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static TrainerSummaryDto getNewTrainer() {
        return new TrainerSummaryDto("jim.carrey", "Jim", "Carrey", "true", new ArrayList<>());
    }
}
