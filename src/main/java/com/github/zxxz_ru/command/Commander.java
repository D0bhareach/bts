package com.github.zxxz_ru.command;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Commander {
    void execute(String a);

    default String getCommand(String args, Messenger messenger) {
        Pattern pattern = Pattern.compile("(-{1,2}\\w+)");
        Matcher matcher = pattern.matcher(args);
        String res = "";
        if (matcher.find()) {
            try {
                res = matcher.group(1);
            }catch (IndexOutOfBoundsException e){
                messenger.print(3);
            }
        }
        return res;

    }

    /**
     * Can return 0 must check for it before use result of this method.
     * @param args
     * @param messenger must pass Messenger to use it.
     * @return POSSIBLE ZERO check function result before use!
     */
    default int getId(String args, Messenger messenger) {
        Pattern pattern = Pattern.compile("(-{1,2}id)(\\s(\\d+))?");
        Matcher matcher = pattern.matcher(args);
        int value = 0;
        if (matcher.find()) {
            try {
                String tmp = matcher.group(2);
                tmp = tmp != null ? tmp.trim() : "No integer!"; // do not pass null to parser
                value = Integer.parseInt(tmp);
            } catch (RuntimeException e) {
                // do not want to fall will ask user to check input.
                messenger.print(3);
                String[] messages = {"Check id parameter's value"};
                messenger.print(4, messages);
            }
        }
        return value;
    }
}