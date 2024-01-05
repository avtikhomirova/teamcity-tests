package com.example.teamcity.api.generators;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomData {
    public enum StringType {
        RANDOM_STRING,
        ALPHANUMERIC_UPPER_CASE;
    }

    private static final int DEFAULT_LENGTH = 10;

    public static String getString(int length, StringType stringType) {
        return switch (stringType) {
            case RANDOM_STRING -> "test_" + RandomStringUtils.randomAlphanumeric(length);
            case ALPHANUMERIC_UPPER_CASE -> RandomStringUtils.randomAlphanumeric(length).toUpperCase();
        };
    }

    public static String getString() {
        return "test_" + RandomStringUtils.randomAlphanumeric(DEFAULT_LENGTH);
    }

}
