package org.example.controllers;

import java.util.Arrays;
import java.util.function.Function;

public class Controller {
    protected static final Function<String[], String> joiningWithComma = array -> String.join(", ", array);
    protected static final Function<String[], String> joiningAsQuestionMark = array -> {
        return String.join(", ", Arrays.stream(array)
                .map(parameter -> "?")
                .toArray(String[]::new));
    };
    protected static final Function<String[], String> joiningWithQuestionMarkAndComma = array -> {
        return String.join(", ", Arrays.stream(array)
                .map(parameter -> parameter + " = ?")
                .toArray(String[]::new));
    };
}
