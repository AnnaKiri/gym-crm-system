package com.annakirillova.trainerworkloadservice;

import com.annakirillova.trainerworkloadservice.model.Trainer;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestData {
    public static final int TRAINER_1_ID = 1;
    public static final String TRAINER_1_USERNAME = "Tom.Cruise";
    public static final String TRAINER_2_USERNAME = "Brad.Pitt";
    public static final String NOT_FOUND_USERNAME = "Not.Found";

    public static final Trainer.Summary SUMMARY_1 = new Trainer.Summary(2024, 1, 60); // TRAINER_4
    public static final Trainer.Summary SUMMARY_2 = new Trainer.Summary(2024, 2, 60); // TRAINER_4
    public static final Trainer.Summary SUMMARY_3 = new Trainer.Summary(2024, 1, 120); // TRAINER_2
    public static final Trainer.Summary SUMMARY_4 = new Trainer.Summary(2024, 2, 60); // TRAINER_2
    public static final Trainer.Summary SUMMARY_5 = new Trainer.Summary(2024, 1, 120); // TRAINER_3
    public static final Trainer.Summary SUMMARY_6 = new Trainer.Summary(2024, 1, 60); // TRAINER_1
    public static final Trainer.Summary SUMMARY_7 = new Trainer.Summary(2024, 2, 60); // TRAINER_1

    public static final List<Trainer.Summary> SUMMARY_LIST_FOR_TRAINER_1 = List.of(SUMMARY_6, SUMMARY_7);
    public static final List<Trainer.Summary> SUMMARY_LIST_FOR_TRAINER_2 = List.of(SUMMARY_3, SUMMARY_4);

    public static final Trainer TRAINER_1 = new Trainer("Tom.Cruise", "Tom", "Cruise", true, new ArrayList<>(SUMMARY_LIST_FOR_TRAINER_1));
    public static final Trainer TRAINER_2 = new Trainer("Brad.Pitt", "Brad", "Pitt", true, SUMMARY_LIST_FOR_TRAINER_2);
    public static final Trainer TRAINER_3 = new Trainer("Jennifer.Aniston", "Jennifer", "Aniston", true, new ArrayList<>());
    public static final Trainer TRAINER_4 = new Trainer("Sandra.Bullock", "Sandra", "Bullock", true, new ArrayList<>());

    public static final MatcherFactory.Matcher<Trainer> TRAINER_MATCHER_WITH_SUMMARY_LIST =
            MatcherFactory.usingAssertions(Trainer.class,
                    (a, e) -> assertThat(a).usingRecursiveComparison().isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static Trainer getNewTrainer() {
        return new Trainer("Jim.Carrey", "Jim", "Carrey", true, new ArrayList<>());
    }
}
