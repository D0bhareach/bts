package com.github.zxxz_ru.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.WebApplicationContextServletContextAwareProcessor;
import org.springframework.stereotype.Component;

@Component
public class Dispatcher {
    private HelpCommand helpCommand;
    private ProjectCommand projectCommand;
    private TaskCommand taskCommand;
    private UserCommand userCommand;

    @Autowired
    Dispatcher(HelpCommand hc, ProjectCommand pc, TaskCommand tc, UserCommand uc){
        this.projectCommand = pc;
        this.taskCommand = tc;
        this.userCommand = uc;
        this.helpCommand = hc;
    }

    public void dispatch(String... args) {
        System.out.println("Start dispatching");
        String command;
        if (args.length == 1) {
            command = args[0];
            if (command.equals("-h") || command.equals("--help")) {
                helpCommand.printAdvise();
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
            helpCommand.printAdvise();
        }
    }

}