package com.github.zxxz_ru.command;

import com.github.zxxz_ru.AppState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


@Component
public class Dispatcher {
    private ProjectCommand projectCommand;
    private TaskCommand taskCommand;
    private UserCommand userCommand;
    private Messenger messenger;
    private AppState appState;

    @Autowired
    Dispatcher(ProjectCommand pc, TaskCommand tc, UserCommand uc, Messenger ms, AppState appst){
        this.projectCommand = pc;
        this.taskCommand = tc;
        this.userCommand = uc;
        this.messenger = ms;
        this.appState = appst;
    }

    public void dispatch(){
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        System.out.println("Scanned: " + str);

    }

    /**
     * Maps commands recognized by double dash (--), makes dashes obligatory for parameters at
     * Application start time.
     * @param args String[]
     * @return HashMap  which is mapping command to it's value as String, numerical values may require parsing.
     */
    public HashMap<String, String> getArgsMap(String... args){
        HashMap<String, String> map = new HashMap<>();
        String command = "";
        for(int i = 0; i < args.length; ++i){
            String str = args[i];
            if(str.contains("--")){
                command= str;
                continue;
            }
            if(i%2 != 0 && ! command.equals("") && !str.equals("")) {
                map.put(command, str);
            }
        }
        return map;
    }

    /**
     * Used to dispatch user input when Application is started.
     * @param args String Array
     */
    public void dispatch(String... args) {
        for (String s : args){
            String command = "";
            if(s.contains("--")){
                command = s;
                break;
            }
            if(command.equals("--mode") ){
                if(s.equals("file")) {
                    appState.setMode(AppState.AppMode.FILESYSTEM);
                } else if (s.equals("database")){
                    appState.setMode(AppState.AppMode.DATABASE);
                }
            }else if(command.equals("--path")){
                // appState.setPath();
            }
            System.out.println(s);
        }
        // TODO: need to get parameters out from args
        String command;
        if (args.length == 1) {
            command = args[0];
            if (command.equals("-h") || command.equals("--help")) {
                messenger.printHelp();
            }
            else if(command.equals("quit")){
                System.exit(0);
            }
            else {
                messenger.printAdvice();
            }
        }
        /*
        else if (args.length >= 2) {
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
        if(command.contains("--mode") || command.contains("--path")){
           //  stateCommand.execute(args);
        }
    }

        else{
            messenger.printAdvice();
        }

         */
    }

}