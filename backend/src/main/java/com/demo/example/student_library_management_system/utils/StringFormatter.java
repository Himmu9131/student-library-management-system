package com.demo.example.student_library_management_system.utils;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;
@Service
public class StringFormatter {

    public static String formatToCleanCapitalized(String input) {
        if (input == null || input.isBlank()) return input;

        return Arrays.stream(input.trim().split("\\s+"))
                .map(word -> {
                    if (Character.isLetter(word.charAt(0))) {
                        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
                    } else {
                        return word;
                    }
                })
                .collect(Collectors.joining(" "));
    }

    public static void formatAllStringFields(Object obj) {
        if (obj == null) return;

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.getType() == String.class) {
                field.setAccessible(true);
                try {
                    String value = (String) field.get(obj);
                    if (value != null && !value.isBlank()) {
                        field.set(obj, formatToCleanCapitalized(value));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // log error if needed
                }
            }
        }
    }
}

