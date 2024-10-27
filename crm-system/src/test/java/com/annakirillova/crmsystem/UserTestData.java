package com.annakirillova.crmsystem;

import com.annakirillova.common.dto.UserDto;
import com.annakirillova.crmsystem.models.User;
import com.annakirillova.crmsystem.util.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserTestData {
    public static final String USER_1_USERNAME = "angelina.jolie";
    public static final String USER_5_USERNAME = "tom.cruise";
    public static final String NOT_FOUND_USERNAME = "Not.Found";

    public static final User USER_1 = new User(1, "Angelina", "Jolie", "angelina.jolie", true);
    public static final User USER_2 = new User(2, "Ryan", "Reynolds", "ryan.reynolds", true);
    public static final User USER_3 = new User(3, "Tom", "Hardy", "tom.hardy", true);
    public static final User USER_4 = new User(4, "Keanu", "Reeves", "keanu.reeves", true);
    public static final User USER_5 = new User(5, "Tom", "Cruise", "tom.cruise", true);
    public static final User USER_6 = new User(6, "Brad", "Pitt", "brad.pitt", true);
    public static final User USER_7 = new User(7, "Jennifer", "Aniston", "jennifer.aniston", true);
    public static final User USER_8 = new User(8, "Sandra", "Bullock", "sandra.bullock", true);
    public static final User USER_9 = new User(9, "Matthew", "Mcconaughey", "matthew.mcconaughey", true);

    public static final Map<String, String> USERS_PASSWORDS = new HashMap<>(Map.of(
            USER_1.getUsername(), "password1",
            USER_2.getUsername(), "password2",
            USER_3.getUsername(), "password3",
            USER_4.getUsername(), "password4",
            USER_5.getUsername(), "password5",
            USER_6.getUsername(), "password6",
            USER_7.getUsername(), "password7",
            USER_8.getUsername(), "password8",
            USER_9.getUsername(), "password9"
    ));
    public static final List<User> USER_LIST = List.of(USER_1, USER_2, USER_3, USER_4, USER_5, USER_6, USER_7, USER_8);

    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingEqualsComparator(User.class);
    public static final MatcherFactory.Matcher<UserDto> USER_DTO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(UserDto.class, "newPassword");

    public static User getNewUser() {
        return new User(null, "Jim", "Carrey", "jim.carrey", true);
    }

    public static User getUpdatedUser() {
        return new User(1, "Ryan", "Reeves", "ryan.reeves", false);
    }

    public static String jsonWithPassword(UserDto userDto, String passw) {
        return JsonUtil.writeAdditionProps(userDto, "newPassword", passw);
    }
}
