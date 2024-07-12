package com.kirillova.gymcrmsystem.util;

import java.security.SecureRandom;
import java.util.Set;

public class UserUtil {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;
    private static final SecureRandom random = new SecureRandom();

    public static String generatePassword() {
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String generateUsername(String firstName, String lastName, Set<String> usernames) {
        String username = firstName + "." + lastName;
        if (usernames.contains(username)) {
            int i = 1;
            while (usernames.contains(username + i)) {
                i++;
            }
            username = username + i;
        }
        usernames.add(username);
        return username;
    }
}
