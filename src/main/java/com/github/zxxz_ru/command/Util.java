package com.github.zxxz_ru.command;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@Component
public class Util<T> {
    /**
     * Used to get value from pair after equal sign.
     *
     * @param pair String of parameter and value must have equal sign.
     * @return String value of pair.
     * @throws NoSuchElementException
     */
    private String parseSingleParameter(String pair) throws NoSuchElementException {
        int index = pair.lastIndexOf("=");
        if (index < 0) throw new NoSuchElementException("Incorrect usage [ = ] is missing.");
        String param = pair.substring(index);
        if (param.length() == 0) throw new NoSuchElementException("No Parameter after equality Character.");
        return param;
    }

    // TODO: may need to change String Array argument to String, as Scanner will read string.

    /**
     * Used to get parameter form String Array.
     *
     * @param parameter
     * @param args
     * @return
     * @throws NoSuchElementException
     */
    public String getParameter(String parameter, String... args) throws NoSuchElementException {
        // Check if there is equality sign
        if (!Arrays.stream(args).anyMatch(s -> Pattern.matches(".*(=){1}.*", s)) && args.length == 2) {
            return args[1];
        }
        String rex = parameter + "(=){1}.*";
        return Arrays.stream(args).filter((v) -> Pattern.matches(rex, v))
                .map(this::parseSingleParameter)
                .findFirst().get();
        //.reduce("", (a,b)->a+b);
    }
}
