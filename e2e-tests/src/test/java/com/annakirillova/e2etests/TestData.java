package com.annakirillova.e2etests;

import java.util.HashMap;
import java.util.Map;

public class TestData {
    public static final String TRAINEE_1_USERNAME = "angelina.jolie";
    public static final String TRAINER_1_USERNAME = "tom.cruise";
    public static final String TRAINER_2_USERNAME = "brad.pitt";
    public static final String NOT_FOUND_USERNAME = "not.found";

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
}
