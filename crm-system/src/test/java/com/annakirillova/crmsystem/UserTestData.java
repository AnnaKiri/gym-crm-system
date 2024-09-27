package com.annakirillova.crmsystem;

import com.annakirillova.crmsystem.dto.UserDto;
import com.annakirillova.crmsystem.models.User;
import com.annakirillova.crmsystem.util.JsonUtil;

import java.util.List;

public class UserTestData {
    public static final String USER_1_USERNAME = "Angelina.Jolie";
    public static final String USER_5_USERNAME = "Tom.Cruise";
    public static final String NOT_FOUND_USERNAME = "Not.Found";

    public static final User USER_1 = new User(1, "Angelina", "Jolie", "Angelina.Jolie", "{noop}password1", true);
    public static final User USER_2 = new User(2, "Ryan", "Reynolds", "Ryan.Reynolds", "{noop}password2", true);
    public static final User USER_3 = new User(3, "Tom", "Hardy", "Tom.Hardy", "{noop}password3", true);
    public static final User USER_4 = new User(4, "Keanu", "Reeves", "Keanu.Reeves", "{noop}password4", true);
    public static final User USER_5 = new User(5, "Tom", "Cruise", "Tom.Cruise", "{noop}password5", true);
    public static final User USER_6 = new User(6, "Brad", "Pitt", "Brad.Pitt", "{noop}password6", true);
    public static final User USER_7 = new User(7, "Jennifer", "Aniston", "Jennifer.Aniston", "{noop}password7", true);
    public static final User USER_8 = new User(8, "Sandra", "Bullock", "Sandra.Bullock", "{noop}password8", true);
    public static final User USER_9 = new User(9, "Matthew", "Mcconaughey", "Matthew.Mcconaughey", "{noop}password9", true);

    public static final List<User> USER_LIST = List.of(USER_1, USER_2, USER_3, USER_4, USER_5, USER_6, USER_7, USER_8);

    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingEqualsComparator(User.class);
    public static final MatcherFactory.Matcher<UserDto> USER_DTO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(UserDto.class, "newPassword");

    public static User getNewUser() {
        return new User(null, "Jim", "Carrey", "Jim.Carrey", "password10", true);
    }

    public static User getUpdatedUser() {
        return new User(1, "Ryan", "Reeves", "Ryan.Reeves", "password11", false);
    }

    public static String jsonWithPassword(UserDto userDto, String passw) {
        return JsonUtil.writeAdditionProps(userDto, "newPassword", passw);
    }
}
