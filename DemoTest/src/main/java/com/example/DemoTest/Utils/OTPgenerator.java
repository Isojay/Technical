package com.example.DemoTest.Utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OTPgenerator {
    private static final int LENGTH = 5;
    private static final String NUMBERS = "0123456789";
    private final Random RANDOM = new Random();

    public String generateRandomString() {
        StringBuilder randomStringBuilder = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(NUMBERS.length());
            char randomChar = NUMBERS.charAt(randomIndex);
            randomStringBuilder.append(randomChar);
        }

        return randomStringBuilder.toString();
    }
}