package com.annakirillova.trainerworkloadservice;

import com.annakirillova.common.dto.TrainerSummaryDto;
import com.annakirillova.trainerworkloadservice.model.TrainerSummary;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestData {
    public static final int TRAINER_1_ID = 1;
    public static final String TRAINER_1_USERNAME = "Tom.Cruise";
    public static final String TRAINER_2_USERNAME = "Brad.Pitt";
    public static final String NOT_FOUND_USERNAME = "Not.Found";

    public static final TrainerSummaryDto.Summary SUMMARY_1 = new TrainerSummaryDto.Summary(2024, 1, 60); // TRAINER_4
    public static final TrainerSummaryDto.Summary SUMMARY_2 = new TrainerSummaryDto.Summary(2024, 2, 60); // TRAINER_4
    public static final TrainerSummaryDto.Summary SUMMARY_3 = new TrainerSummaryDto.Summary(2024, 1, 120); // TRAINER_2
    public static final TrainerSummaryDto.Summary SUMMARY_4 = new TrainerSummaryDto.Summary(2024, 2, 60); // TRAINER_2
    public static final TrainerSummaryDto.Summary SUMMARY_5 = new TrainerSummaryDto.Summary(2024, 1, 120); // TRAINER_3
    public static final TrainerSummaryDto.Summary SUMMARY_6 = new TrainerSummaryDto.Summary(2024, 1, 60); // TRAINER_1
    public static final TrainerSummaryDto.Summary SUMMARY_7 = new TrainerSummaryDto.Summary(2024, 2, 60); // TRAINER_1

    public static final List<TrainerSummaryDto.Summary> SUMMARY_LIST_FOR_TRAINER_1 = List.of(SUMMARY_6, SUMMARY_7);
    public static final List<TrainerSummaryDto.Summary> SUMMARY_LIST_FOR_TRAINER_2 = List.of(SUMMARY_3, SUMMARY_4);

    public static final TrainerSummary TRAINER_SUMMARY_1 = new TrainerSummary("Tom.Cruise", "Tom", "Cruise", true, new ArrayList<>(SUMMARY_LIST_FOR_TRAINER_1));
    public static final TrainerSummary TRAINER_SUMMARY_2 = new TrainerSummary("Brad.Pitt", "Brad", "Pitt", true, SUMMARY_LIST_FOR_TRAINER_2);
    public static final TrainerSummary TRAINER_SUMMARY_3 = new TrainerSummary("Jennifer.Aniston", "Jennifer", "Aniston", true, new ArrayList<>());
    public static final TrainerSummary TRAINER_SUMMARY_4 = new TrainerSummary("Sandra.Bullock", "Sandra", "Bullock", true, new ArrayList<>());

    public static final MatcherFactory.Matcher<TrainerSummary> TRAINER_MATCHER_WITH_SUMMARY_LIST =
            MatcherFactory.usingAssertions(TrainerSummary.class,
                    (a, e) -> assertThat(a).usingRecursiveComparison().isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static TrainerSummary getNewTrainer() {
        return new TrainerSummary("Jim.Carrey", "Jim", "Carrey", true, new ArrayList<>());
    }
}
