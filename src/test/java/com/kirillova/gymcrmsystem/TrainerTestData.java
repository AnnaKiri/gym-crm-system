package com.kirillova.gymcrmsystem;

import com.kirillova.gymcrmsystem.models.Trainer;

import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_1;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_2;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_3;
import static com.kirillova.gymcrmsystem.TrainingTypeTestData.TRAINING_TYPE_4;
import static com.kirillova.gymcrmsystem.UserTestData.USER_5;
import static com.kirillova.gymcrmsystem.UserTestData.USER_6;
import static com.kirillova.gymcrmsystem.UserTestData.USER_7;
import static com.kirillova.gymcrmsystem.UserTestData.USER_8;

public class TrainerTestData {
    public static final Trainer TRAINER_1 = new Trainer(1, TRAINING_TYPE_1, USER_5);
    public static final Trainer TRAINER_2 = new Trainer(2, TRAINING_TYPE_2, USER_6);
    public static final Trainer TRAINER_3 = new Trainer(3, TRAINING_TYPE_3, USER_7);
    public static final Trainer TRAINER_4 = new Trainer(4, TRAINING_TYPE_4, USER_8);

    public static final MatcherFactory.Matcher<Trainer> TRAINER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Trainer.class, "user");

    public static Trainer getNewTrainer() {
        return new Trainer(null, TRAINING_TYPE_4, USER_6);
    }

    public static Trainer getUpdatedTrainer() {
        return new Trainer(1, TRAINING_TYPE_4, USER_5);
    }
}
