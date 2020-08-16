package com.github.zxxz_ru.command;

import com.github.zxxz_ru.entity.StoreUnit;
import com.github.zxxz_ru.entity.Task;
import com.github.zxxz_ru.entity.User;
import com.github.zxxz_ru.storage.file.TaskFileRepository;
import com.github.zxxz_ru.storage.file.UserFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserCommand implements Commander<User> {

    @Autowired
    TaskCommand taskCommand;
    @Autowired
    UserFileRepository repository;
    @Autowired
    TaskFileRepository taskRepository;
    @Autowired
    Messenger messenger;

    /**
     * Get parameters from --update command arguments, create new user and set fields to argument's values
     * Does not set id if it's not provided instead it set id to -1 affectively creating new user.
     * FileSystemRepository save(S entity) method will check for existence and availability if
     * id in saved Entities and change fields that provided.
     *
     * @param args command line
     * @return new User fields that not set have null value. Except id it either -1 or real id from command.
     * See save method from  FileSystemRepository.
     */
    private User setUserForUpdate(String args) {
        List<String> parameters = List.of("id", "firstname", "lastname", "role");
        User user = new User();
        for (String parameter : parameters) {
            Pattern pattern = preparePattern(parameter);
            Matcher matcher = pattern.matcher(args);
            if (matcher.find()) {
                switch (parameter) {
                    case "id":
                        String id = matcher.group(3);
                        if (id != null) {
                            user.setId(Integer.parseInt(id));
                        } else {
                            // in save method it will trigger new User
                            user.setId(-1);
                        }
                        break;
                    case "firstname":
                        user.setFirstName(matcher.group(3));
                        break;
                    case "lastname":
                        user.setLastName(matcher.group(3));
                        break;
                    case "role":
                        user.setRole(matcher.group(3));
                        break;
                }
            }

        }
        // trigger new user in save method
        if (user.getId() == null) user.setId(-1);
        return user;
    }

    /**
     * @param args   command line
     * @param prefix prefix to regexp for finding task part in user's command line
     * @param id     id extracted from user's command line
     * @return true if all goes well, false otherwise
     */
    @SuppressWarnings("OptionalIsPresent")
    private Optional<List<? extends StoreUnit>> processTaskCommand(String args, String prefix, int id) {
        Optional<List<? extends StoreUnit>> empty = Optional.empty();
        String pattern = new StringBuilder(prefix).append("-task\\s+(\\d+)").substring(0);
        int taskId;
        Pattern p1 = Pattern.compile(pattern);
        Matcher m1 = p1.matcher(args);
        if (m1.find()) {
            String idString = m1.group(1).trim();
            try {
                taskId = Integer.parseInt(idString);
            } catch (NumberFormatException e) {
                messenger.print("Check task id.");
                return empty;
            }
            if (taskId == 0) {
                messenger.print(3);
                messenger.print("Check task id.");
                return empty;
            }
            if (prefix.equals("--assign")) {
                taskCommand.execute(
                        new StringBuilder("task -id ")
                                .append(idString)
                                .append(" --add-user ").append(id).substring(0));
                Optional<Task> optionalTask = taskRepository.findById(taskId);
                if (optionalTask.isPresent()) {
                    return Optional.of(List.of(optionalTask.get()));
                }
                return empty;
            } else if (prefix.equals("--drop")) {
                taskCommand.execute(
                        new StringBuilder("task -id ")
                                .append(idString)
                                .append(" --remove-user ").append(id).substring(0));
                Optional<Task> optionalTask = taskRepository.findById(taskId);
                if (optionalTask.isPresent()) {
                    return Optional.of(List.of(optionalTask.get()));
                }

                return empty;

            }
        }
        messenger.print(3);
        messenger.print("Check task id.");
        return empty;
    }


    /**
     * Goes through cases of command line commands. Resolve each case, perform actions.
     *
     * @param args command line
     */
    @Override
    public Optional<List<? extends StoreUnit>> execute(String args) {
        Matcher idMatcher = Pattern.compile("^user\\s+-id\\s+(\\d+)$").matcher(args.trim());
        Matcher assignMatcher = Pattern.compile("^user\\s+-id\\s+(\\d+)\\s+--assign-task\\s+\\d+").matcher(args.trim());
        Matcher dropMatcher = Pattern.compile("^user\\s+-id\\s+(\\d+)\\s+--drop-task\\s+\\d+").matcher(args.trim());
        Optional<List<? extends StoreUnit>> empty = Optional.empty();
        int id = -1;
        String command = getCommand(args, messenger);
        switch (command) {
            case "-a":
            case "--all":
                return Optional.of((List<User>) repository.findAll());
            case "-d":
            case "--delete":
                id = getId(args, messenger);
                if (id != 0) {
                    repository.deleteById(id);
                    return empty;
                }
                break;
            case "--update":
                User user = setUserForUpdate(args);
                return Optional.of(List.of(repository.save(user)));
            case "-id":
                id = getId(args, messenger);
                if (id == 0) {
                    return empty;
                }
                if (idMatcher.find()) {
                    Optional<User> opti = repository.findById(id);
                    if (opti.isPresent()) {
                        return Optional.of((List.of(opti.get())));
                    }
                }
                if (assignMatcher.find()) {
                    Optional<List<? extends StoreUnit>> res = processTaskCommand(args, "--assign", id);
                    if (!res.isEmpty()) {
                        return res;
                    }
                }
                if (dropMatcher.find()) {
                    Optional<List<? extends StoreUnit>> res = processTaskCommand(args, "--drop", id);
                    if (!res.isEmpty()) {
                        return res;
                    }
                }
        }
        return empty;
    }
}
