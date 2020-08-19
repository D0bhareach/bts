package com.github.zxxz_ru.command;

import com.github.zxxz_ru.AppState;
import com.github.zxxz_ru.ApplicationCloser;
import com.github.zxxz_ru.storage.InitialDataInserter;
import com.github.zxxz_ru.storage.dao.DatabaseRepositoryCreator;
import com.github.zxxz_ru.storage.file.FileSystemRepositoryCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class Dispatcher {
    @Autowired
    private Messenger messenger;
    @Autowired
    private AppState appState;
    @Autowired
    private ApplicationCloser closer;
    @Autowired
    private InitialDataInserter inserter;
    @Autowired
    FileSystemRepositoryCreator fileSystemRepositoryCreator;
    @Autowired
    DatabaseRepositoryCreator databaseRepositoryCreator;
    @Autowired
    UserCommand userCommand;
    @Autowired
    TaskCommand taskCommand;
    @Autowired
    ProjectCommand projectCommand;

    @Value("${default.database.dirpath}")
    private String databasePath;


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

    /**
     * Used to dispatch user input when Application is started.
     *
     * @param args String Array
     */
    public void dispatch(String... args) {
        if (args.length == 0) {
            userCommand.init(fileSystemRepositoryCreator);
            taskCommand.init(fileSystemRepositoryCreator);
            projectCommand.init(fileSystemRepositoryCreator);
            databaseRepositoryCreator = null;
            messenger.print(2);
            return;
        }
        String command;
        // One argument it's either help or quit.
        if (args.length >= 1) {
            command = args[0];
            if (command.equals("--database")) {
                // appState.setMode(AppState.AppMode.DATABASE);
                userCommand.init(databaseRepositoryCreator);
                taskCommand.init(databaseRepositoryCreator);
                projectCommand.init(databaseRepositoryCreator);
                fileSystemRepositoryCreator = null;
                if (databasePath != null) {
                    // The very first time throws exception FileNotExist
                    // so need this check.
                    if (!Files.exists(Paths.get(databasePath))) {
                        inserter.insert();
                        messenger.print(2);
                        return;
                    }
                    try {
                        if (Files.list(Paths.get(databasePath)).count() <= 0) {
                            inserter.insert();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                messenger.print(2);
                return;
            } else if (command.equals("--filepath")) {
                // appState.setMode(AppState.AppMode.FILESYSTEM);
                userCommand.init(fileSystemRepositoryCreator);
                taskCommand.init(fileSystemRepositoryCreator);
                projectCommand.init(fileSystemRepositoryCreator);
                databaseRepositoryCreator = null;
                if (args.length == 2) {
                    String path = args[1];
                    if (!path.equals("")) {
                        messenger.print(path);
                    }
                }
                messenger.print(2);
                return;
            } else if (command.equals("-h") || command.equals("--help") || command.equals("help")) {
                messenger.printHelp();
                closer.closeApp(0);
            }
            // no short for exit command.
            // keep it for consistency, although it's kinda stupid here.
            else if (command.equals("quit") || command.equals("--quit")) {
                closer.closeApp(0);
            }
        }
    }


    public void dispatch() {
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        if (str.trim().equals("")) {
            return;
        } else {
            // search for dash commands
            Pattern pattern = Pattern.compile("^\\p{Blank}*?-{1,2}+\\w++");
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                String command;
                command = matcher.group().trim();
                if (command.equals("-h") || command.equals("--help")) {
                    messenger.printHelp();
                } else if (command.equals("--quit")) {
                    scan.close();
                    closer.closeApp(0);
                }
            }
            // commands without dashes
            pattern = Pattern.compile("^\\p{Blank}*?\\w++");
            matcher = pattern.matcher(str);
            while (matcher.find()) {
                switch (matcher.group().trim()) {
                    case "user":
                        userCommand.execute(str).ifPresent(messenger::print);
                        break;
                    case "task":
                        taskCommand.execute(str).ifPresent(messenger::print);
                        break;
                    case "project":
                        projectCommand.execute(str).ifPresent(messenger::print);
                        break;
                    case "help":
                        messenger.printHelp();
                        break;
                    case "quit":
                        scan.close();
                        closer.closeApp(0);
                }
            }
        }
    }

}