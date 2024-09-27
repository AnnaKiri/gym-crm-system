package com.annakirillova.trainerworkloadservice;

import com.annakirillova.trainerworkloadservice.dto.TrainerDto;
import com.annakirillova.trainerworkloadservice.model.Trainer;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TrainerTestData {
    public static final int TRAINER_1_ID = 1;
    public static final String TRAINER_1_USERNAME = "Tom.Cruise";
    public static final String NOT_FOUND_USERNAME = "Not.Found";

    public static final Trainer TRAINER_1 = new Trainer(1, "Tom.Cruise", "Tom", "Cruise", true);
    public static final Trainer TRAINER_2 = new Trainer(2, "Brad.Pitt", "Brad", "Pitt", true);
    public static final Trainer TRAINER_3 = new Trainer(3, "Jennifer.Aniston", "Jennifer", "Aniston", true);
    public static final Trainer TRAINER_4 = new Trainer(4, "Sandra.Bullock", "Sandra", "Bullock", true);

    public static final Map<Integer, Map<String, Integer>> yearMonthlySummary = new HashMap<>();

    static {
        Map<String, Integer> summary2024 = new HashMap<>();
        summary2024.put("January", 120);
        summary2024.put("February", 60);

        yearMonthlySummary.put(2024, summary2024);
    }

    public static final TrainerDto TRAINER_DTO = TrainerDto.builder()
            .username(TRAINER_2.getUsername())
            .firstName(TRAINER_2.getFirstName())
            .lastName(TRAINER_2.getLastName())
            .isActive(TRAINER_2.isActive())
            .monthlySummary(yearMonthlySummary)
            .build();

    public static final MatcherFactory.Matcher<Trainer> TRAINER_MATCHER = MatcherFactory.usingEqualsComparator(Trainer.class);
    public static final MatcherFactory.Matcher<TrainerDto> TRAINER_DTO_MATCHER = MatcherFactory.usingEqualsComparator(TrainerDto.class);

    public static final MatcherFactory.Matcher<TrainerDto> TRAINER_DTO_MATCHER_WITH_SUMMARY =
            MatcherFactory.usingAssertions(TrainerDto.class,
                    (actual, expected) -> assertThat(actual)
                            .usingRecursiveComparison()
                            .isEqualTo(expected),
                    (actual, expected) -> {
                        throw new UnsupportedOperationException();
                    });

    public static Trainer getNewTrainer() {
        return new Trainer(null, "Jim.Carrey", "Jim", "Carrey", true);
    }
}
