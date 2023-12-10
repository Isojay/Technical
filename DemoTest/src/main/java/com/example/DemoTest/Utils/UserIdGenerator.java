package com.example.DemoTest.Utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UserIdGenerator {

    private static final int LENGTH = 5;

    public String generateRandomString() {
        String numbers = "0123456789";
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder randomStringBuilder = new StringBuilder(LENGTH);
        Random random = new Random();

        for (int i = 0; i < LENGTH; i++) {
            char randomChar;
            if(i < 2){
                int randomIndex = random.nextInt(numbers.length());
                randomChar = numbers.charAt(randomIndex);
            }else {
                int randomIndex = random.nextInt(characters.length());
                randomChar = characters.charAt(randomIndex);
            }
            randomStringBuilder.append(randomChar);
        }

        return randomStringBuilder.toString();
    }
}