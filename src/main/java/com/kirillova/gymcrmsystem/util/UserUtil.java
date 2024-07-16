package com.kirillova.gymcrmsystem.util;

import java.security.SecureRandom;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String generateUsername(String firstName, String lastName, List<String> usernames) {

        String username = firstName + "." + lastName;

        if (usernames.isEmpty()) {
            return username;
        } else {
            String lastUsername = usernames.get(usernames.size() - 1);
            Pattern pattern = Pattern.compile(Pattern.quote(username) + "(\\d+)$");
            Matcher matcher = pattern.matcher(lastUsername);
            int lastNumber = 0;
            if (matcher.find()) {
                lastNumber = Integer.parseInt(matcher.group(1));
            }
            return username + (lastNumber + 1);
        }
    }
}
