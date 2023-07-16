package com.example.finalproject.Utility;

import java.util.regex.Pattern;

public class RegexUtils {

    private static final String EMAIL_PATTERN = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValidEmail(CharSequence email) {
        return pattern.matcher(email).matches();
    }

    public static boolean isValidPrice(CharSequence price) {
        String priceString = price.toString();
        return priceString.matches("^\\d+(\\.\\d{1,2})?$");
    }
}
