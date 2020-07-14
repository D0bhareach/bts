package com.github.zxxz_ru.command;

public interface Commander {
    default void execute(String... a){
        if(a.length > 0){
            for (String str : a){
                System.out.println("\t> " + str);
            }
        }
        System.exit(0);
    }
}
