package com.github.zxxz_ru.command;

import com.github.zxxz_ru.AppState;
import com.github.zxxz_ru.ApplicationCloser;
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
    private ApplicationCloser closer;

    @Autowired
    Dispatcher(ProjectCommand pc, TaskCommand tc, UserCommand uc, Messenger ms, AppState appst, ApplicationCloser closer) {
        this.projectCommand = pc;
        this.taskCommand = tc;
        this.userCommand = uc;
        this.messenger = ms;
        this.appState = appst;
        this.closer = closer;
    }


    /**
     * Maps commands recognized by double dash (--), makes dashes obligatory for parameters at
     * Application start time.
     *
     * @param args String[]
     * @return HashMap  which is mapping command to it's value as String, numerical values may require parsing.
     */
    public HashMap<String, String> getArgsMap(String... args) {
        HashMap<String, String> map = new HashMap<>();
        String command = "";
        for (int i = 0; i < args.length; ++i) {
            String str = args[i];
            if (str.contains("--")) {
                command = str;
                // Put default empty string in case of value is not required or missing
                map.put(command, "");
                continue;
            }
            if (i % 2 != 0 && !command.equals("") && !str.equals("")) {
                map.put(command, str);
                // ignore double arguments separated with space
                command = "";
            }
        }
        return map;
    }

    public void processCommandMap(Map<String, String> map) {
        if (map.containsKey("--filepath")) {
            // even if it's empty parameter --filepath File System is about to be used with default file
            appState.setMode(AppState.AppMode.FILESYSTEM);
            String path = map.get("--filepath");
            if (!path.equals("")) {
                appState.setPath(path);
            }
        }
        if (map.containsKey("--database")) {
            appState.setMode(AppState.AppMode.DATABASE);
            // do db stuff
        }
    }

    /**
     * Used to dispatch user input when Application is started.
     *
     * @param args String Array
     */
    public void dispatch(String... args) {
        if (args.length == 0) {
            messenger.printAdvice();
            return;
        }
        String command;
        // One argument it's either help or quit.
        if (args.length == 1) {
            command = args[0];
            if (command.equals("-h") || command.equals("--help") || command.equals("help")) {
                closer.closeApp(0);
            }
            // no short for exit command.
            // keep it for consistency, although it's kinda stupid here.
            else if (command.equals("quit") || command.equals("--quit")) {
                closer.closeApp(0);
            }
        } else if (args.length > 1) {
            Map<String, String> map = getArgsMap(args);
            processCommandMap(map);
            messenger.printAdvice();
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
    public void dispatch() {
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        System.out.println("Scanned: " + str);

    }

}