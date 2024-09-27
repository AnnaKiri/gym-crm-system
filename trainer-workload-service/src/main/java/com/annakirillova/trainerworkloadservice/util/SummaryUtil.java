package com.annakirillova.trainerworkloadservice.util;

import com.annakirillova.trainerworkloadservice.dto.SummaryDto;
import com.annakirillova.trainerworkloadservice.model.Summary;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SummaryUtil {

    public static List<SummaryDto> getDtos(List<Summary> summaryList) {
        return summaryList.stream()
                .map(summary -> new SummaryDto(summary.getYear(), summary.getMonth(), summary.getDuration()))
                .collect(Collectors.toList());
    }
}
