package com.github.zxxz_ru.command;

import java.io.PrintStream;
import java.util.List;

class Util<T> {
    private final static PrintStream st = System.out;
    static <T>void print(List<T> list){
        if (list.size() == 0){
            st.println("No Result.");
            return;
        }
        for (T ent : list){
            st.print(ent.toString());
        }

    }
}
