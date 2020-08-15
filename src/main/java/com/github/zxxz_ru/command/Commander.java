package com.github.zxxz_ru.command;

import com.github.zxxz_ru.entity.StoreUnit;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Commander <T extends  StoreUnit>{
    Optional<List<T>> execute(String a);

    default String getCommand(String args, Messenger messenger) {
        Pattern pattern = Pattern.compile("(-{1,2}\\w+)");
        Matcher matcher = pattern.matcher(args);
        String res = "";
        if (matcher.find()) {
            try {
                res = matcher.group(1);
            } catch (IndexOutOfBoundsException e) {
                messenger.print(3);
            }
        }
        return res;

    }

    /**
     * Used to get id parameter as integer when commands are (id|d)
     * Can return 0 must check for it before use result of this method.
     *
     * @param args
     * @param messenger must pass Messenger to use it.
     * @return POSSIBLE ZERO check function result before use!
     */
    default int getId(String args, Messenger messenger) {
        Pattern pattern = Pattern.compile("((-id|-d)|(--delete))+(\\s+(\\d+))?");
        Matcher matcher = pattern.matcher(args);
        int value = 0;
        if (matcher.find()) {
            try {
                String tmp = matcher.group(4);
                tmp = tmp != null ? tmp.trim() : "No integer!"; // do not pass null to parser
                value = Integer.parseInt(tmp);
            } catch (RuntimeException e) {
                // do not want to fall will ask user to check input.
                messenger.print(3);
                messenger.print("Check id parameter's value");
            }
        }
        return value;
    }

    // group(2)
// users\s?=\s?(\{\s?)(.+,?)+(\s?\})
    String end = ")\\s?=\\s?('(\\w+\\s?\\w+)')";
    String idEnd = ")\\s?=\\s?((\\d+))";

    default Pattern preparePattern(String parameter) {
        // id firstname ...
        StringBuilder builder = new StringBuilder("(");
        if (parameter.equals("id")) {
            builder.append("id").append(idEnd);
            return Pattern.compile(builder.substring(0));
        } else if (parameter.equals("users")) {
            return Pattern.compile("users\\s?=\\s?(\\{\\s?)(.+,?)+(\\s?\\})");
        }else if(parameter.equals("tasks")){
            return Pattern.compile("tasks\\s?=\\s?(\\{\\s?)(.+,?)+(\\s?\\})");
        }

        builder.append(parameter).append(end);
        return Pattern.compile(builder.substring(0));
    }


}