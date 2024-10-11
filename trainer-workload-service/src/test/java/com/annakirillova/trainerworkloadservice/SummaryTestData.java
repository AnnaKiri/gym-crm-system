package com.annakirillova.trainerworkloadservice;

import com.annakirillova.trainerworkloadservice.dto.SummaryDto;
import com.annakirillova.trainerworkloadservice.model.Summary;
import com.annakirillova.trainerworkloadservice.util.SummaryUtil;

import java.util.List;

import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_1;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_2;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_3;
import static com.annakirillova.trainerworkloadservice.TrainerTestData.TRAINER_4;

public class SummaryTestData {

    public static final Summary SUMMARY_1 = new Summary(1, TRAINER_4, 2024, 1, 60);
    public static final Summary SUMMARY_2 = new Summary(2, TRAINER_4, 2024, 2, 60);
    public static final Summary SUMMARY_3 = new Summary(3, TRAINER_2, 2024, 1, 120);
    public static final Summary SUMMARY_4 = new Summary(4, TRAINER_2, 2024, 2, 60);
    public static final Summary SUMMARY_5 = new Summary(5, TRAINER_3, 2024, 1, 120);
    public static final Summary SUMMARY_6 = new Summary(6, TRAINER_1, 2024, 1, 60);
    public static final Summary SUMMARY_7 = new Summary(7, TRAINER_1, 2024, 2, 60);

    public static final List<Summary> SUMMARY_LIST_FOR_TRAINER_1 = List.of(SUMMARY_6, SUMMARY_7);
    public static final List<Summary> SUMMARY_LIST_FOR_TRAINER_2 = List.of(SUMMARY_3, SUMMARY_4);
    public static final List<SummaryDto> SUMMARY_DTO_LIST_FOR_TRAINER_2 = SummaryUtil.getDtos(SUMMARY_LIST_FOR_TRAINER_2);

    public static final MatcherFactory.Matcher<Summary> SUMMARY_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Summary.class, "trainer");

    public static List<Summary> getUpdatedSummary() {
        return List.of(new Summary(6, TRAINER_1, 2024, 1, 120));
    }
}
