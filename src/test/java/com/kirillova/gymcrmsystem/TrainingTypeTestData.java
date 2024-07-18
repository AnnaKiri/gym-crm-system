package com.kirillova.gymcrmsystem;

import com.kirillova.gymcrmsystem.models.TrainingType;

public class TrainingTypeTestData {
    public static final TrainingType TRAINING_TYPE_1 = new TrainingType(1, "Strength");
    public static final TrainingType TRAINING_TYPE_2 = new TrainingType(2, "Aerobic");
    public static final TrainingType TRAINING_TYPE_3 = new TrainingType(3, "Yoga");
    public static final TrainingType TRAINING_TYPE_4 = new TrainingType(4, "Stretching");
    public static final MatcherFactory.Matcher<TrainingType> TRAINING_TYPE_MATCHER = MatcherFactory.usingEqualsComparator(TrainingType.class);
}
