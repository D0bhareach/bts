package com.github.zxxz_ru.command;

import org.springframework.stereotype.Component;

@Component
class TaskCommand implements Commander {
@Override
    public void execute(String s){
    System.out.println(s);
}
}
