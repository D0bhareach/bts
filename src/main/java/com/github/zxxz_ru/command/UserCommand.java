package com.github.zxxz_ru.command;

import com.github.zxxz_ru.entity.StoreUnit;
import com.github.zxxz_ru.entity.Task;
import com.github.zxxz_ru.entity.User;
import com.github.zxxz_ru.storage.RepositoryCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserCommand implements Commander<User> {

    @Autowired
    private Messenger messenger;

    private CrudRepository repository;
    private CrudRepository taskRepository;

    public void init(RepositoryCreator repositoryCreator) {
        this.repository = repositoryCreator.getUserRepository();
        this.taskRepository = repositoryCreator.getTaskRepository();
    }


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
                String str = matcher.group(3);
                switch (parameter) {
                    case "id":
                        if (str != null) {
                            Integer userId = Integer.parseInt(str);
                            Optional<User> userOptional = repository.findById(userId);
                            if(userOptional.isPresent()){
                                user = userOptional.get();
                            }else{
                                user.setId(userId);
                            }

                        } else {
                            // in save method it will trigger new User
                            user.setId(-1);
                        }
                        break;
                    case "firstname":
                        if(str != null)
                        user.setFirstName(str);
                        break;
                    case "lastname":
                        if(str != null)
                        user.setLastName(str);
                        break;
                    case "role":
                        if(str != null)
                        user.setRole(str);
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
        Optional<List<? extends StoreUnit>> result = Optional.empty();
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
            Optional<Task> taskOptional = taskRepository.findById(taskId);
            Optional<User> userOptional = repository.findById(id);
            if (taskOptional.isPresent() && userOptional.isPresent()) {
                Task task = taskOptional.get();
                User user = userOptional.get();
                if (prefix.equals("--assign")) {
                    Task newTask = new Task();
                    newTask.setUserList(List.of(user));
                    task = task.from(newTask);
                    task = (Task) taskRepository.save(task);
                    result = Optional.of(List.of(task));
                } else if (prefix.equals("--drop")) {
                    List<User> users = task.getUserList();
                    users.remove(user);
                    task.setUserList(users);
                    task = (Task) taskRepository.save(task);
                    result = Optional.of(List.of(task));
                }
                return result;

            }
            return empty;
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
                return Optional.of(List.of((User) repository.save(user)));
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
