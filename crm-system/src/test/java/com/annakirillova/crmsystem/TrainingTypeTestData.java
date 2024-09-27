package com.annakirillova.crmsystem;

import com.annakirillova.crmsystem.models.TrainingType;

import java.util.List;

public class TrainingTypeTestData {
    public static final int TRAINING_TYPE_1_ID = 1;

    public static final TrainingType TRAINING_TYPE_1 = new TrainingType(1, "Strength");
    public static final TrainingType TRAINING_TYPE_2 = new TrainingType(2, "Aerobic");
    public static final TrainingType TRAINING_TYPE_3 = new TrainingType(3, "Yoga");
    public static final TrainingType TRAINING_TYPE_4 = new TrainingType(4, "Stretching");
    public static final MatcherFactory.Matcher<TrainingType> TRAINING_TYPE_MATCHER = MatcherFactory.usingEqualsComparator(TrainingType.class);
    public static final List<TrainingType> TRAINING_TYPE_LIST = List.of(TRAINING_TYPE_1, TRAINING_TYPE_2, TRAINING_TYPE_3, TRAINING_TYPE_4);
}
