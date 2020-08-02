package com.github.zxxz_ru.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class Dispatcher {
    private ProjectCommand projectCommand;
    private TaskCommand taskCommand;
    private UserCommand userCommand;
    private Messenger msg;

    @Autowired
    Dispatcher(ProjectCommand pc, TaskCommand tc, UserCommand uc, Messenger ms){
        this.projectCommand = pc;
        this.taskCommand = tc;
        this.userCommand = uc;
        this.msg = ms;
    }

    public void dispatch(String... args) {
        String command;
        if (args.length == 1) {
            command = args[0];
            if (command.equals("-h") || command.equals("--help")) {
                msg.printHelp();
            }
            else if(command.equals("quit")){
                System.exit(0);
            }
            else {
                msg.printAdvice();
            }
        } else if (args.length >= 2) {
        command = args[0];
        String[] a = new String[args.length - 1];
        System.arraycopy(args, 1, a, 0, a.length);
        if(command.equals("project")){
           projectCommand.execute(a);
        }
        if(command.equals("task")){
           taskCommand.execute(a);
        }
        if(command.equals("user")){
            userCommand.execute(a);
        }
    }

        else{
            msg.printAdvice();
        }
    }

}