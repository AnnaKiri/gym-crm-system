package com.kirillova.gymcrmsystem;

import com.kirillova.gymcrmsystem.models.User;

public class UserTestData {
    public static final User USER_1 = new User(1, "Angelina", "Jolie", "Angelina.Jolie", "password1", true);
    public static final User USER_2 = new User(2, "Ryan", "Reynolds", "Ryan.Reynolds", "password2", true);
    public static final User USER_3 = new User(3, "Tom", "Hardy", "Tom.Hardy", "password3", true);
    public static final User USER_4 = new User(4, "Keanu", "Reeves", "Keanu.Reeves", "password4", true);
    public static final User USER_5 = new User(5, "Tom", "Cruise", "Tom.Cruise", "password5", true);
    public static final User USER_6 = new User(6, "Brad", "Pitt", "Brad.Pitt", "password6", true);
    public static final User USER_7 = new User(7, "Jennifer", "Aniston", "Jennifer.Aniston", "password7", true);
    public static final User USER_8 = new User(8, "Sandra", "Bullock", "Sandra.Bullock", "password8", true);

    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingEqualsComparator(User.class);

    public static User getNewUser() {
        return new User(null, "Matthew", "Mcconaughey", "Matthew.Mcconaughey", "password9", true);
    }

    public static User getUpdatedUser() {
        return new User(1, "Ryan", "Reeves", "Ryan.Reeves", "password11", false);
    }

}
