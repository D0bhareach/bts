package com.github.zxxz_ru.command;

import com.github.zxxz_ru.entity.StoreUnit;
import com.github.zxxz_ru.entity.Task;
import com.github.zxxz_ru.entity.User;
import com.github.zxxz_ru.storage.RepositoryCreator;
import com.github.zxxz_ru.storage.file.EntityMode;
import com.github.zxxz_ru.storage.file.Storage;
import com.github.zxxz_ru.storage.file.TaskFileRepository;
import com.github.zxxz_ru.storage.file.UserFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class TaskCommand implements Commander<Task> {

    @Autowired
    private Messenger messenger;
    @Autowired
    private Storage storage;

    private CrudRepository repository;
    private CrudRepository userRepository;


    public void init(RepositoryCreator repositoryCreator){
        repository = repositoryCreator.getTaskRepository();
        userRepository = repositoryCreator.getUserRepository();
    }

    /**
     * @param args   command line
     * @param prefix prefix to regexp for finding task part in user's command line
     * @param id     id extracted from user's command line
     * @return true if all goes well, false otherwise
     */
    private Optional<List<? extends StoreUnit>> processUserCommand(String args, String prefix, int id) {
        Optional<List<? extends StoreUnit>> empty = Optional.empty();
        Optional<List<? extends StoreUnit>> result = Optional.empty();
        int userId = 0;
        String pattern = new StringBuilder(prefix).append("-user\\s+(\\d+)").substring(0);
        Pattern p1 = Pattern.compile(pattern);

        Matcher m1 = p1.matcher(args);
        if (m1.find()) {
            String IdString = m1.group(1);
            try {
                userId = Integer.parseInt(IdString.trim());
            } catch (NumberFormatException e) {
                messenger.print("Check task id value.");
                return empty;
            }
            Optional<User> opti = userRepository.findById(userId);
            if (opti.isPresent()) {
                List<Task> tasks = (List<Task>) repository.findAll();
                Optional<Task> optiOld = repository.findById(id);
                if (prefix.equals("--add")) {
                    if (optiOld.isPresent()) {
                        Task newTask = new Task();
                        newTask.setUserList(List.of(opti.get()));
                        Task old = optiOld.get();
                        int indx = tasks.indexOf(old);
                        old = old.from(newTask);
                        tasks.set(indx, old);
                        result = Optional.of(List.of(old));
                    }

                    storage.updateStorageList(tasks, EntityMode.TASK);
                    return result;
                } else if (prefix.equals("--remove")) {
                    if (optiOld.isPresent()) {
                        Task old = optiOld.get();
                        int indx = tasks.indexOf(old);
                        List<User> users = old.getUserList();
                        users.remove(opti.get());
                        old.setUserList(users);
                        tasks.set(indx, old);
                        result = Optional.of(List.of(old));
                    }
                }
                storage.updateStorageList(tasks, EntityMode.TASK);
                return result;
            }
        }
        return empty;
    }

    private Task setTaskForUpdate(String args) {
        List<String> parameters = List.of("id", "theme", "priority", "type", "description", "users");
        Task task = new Task();
        for (String parameter : parameters) {
            Pattern pattern = preparePattern(parameter);
            Matcher matcher = pattern.matcher(args);
            if (matcher.find()) {
                switch (parameter) {
                    case "id":
                        String id = matcher.group(3);
                        if (id != null) {
                            task.setId(Integer.parseInt(id));
                        } else {
                            // in save method it will trigger new User
                            task.setId(-1);
                        }
                        break;
                    case "theme":
                        task.setThema(matcher.group(3));
                        break;
                    case "priority":
                        task.setPriority(matcher.group(3));
                        break;
                    case "type":
                        task.setTaskType(matcher.group(3));
                        break;
                    case "description":
                        task.setDescription(matcher.group(3));
                        break;
                    case "users":
                        List<User> ulist = new ArrayList<>();
                        String ids = matcher.group(2);
                        String[] uids = ids.split(",");
                        for (String s : uids) {
                            try {
                                int userId = Integer.parseInt(s);
                                Optional<User> opti = userRepository.findById(userId);
                                opti.ifPresent(ulist::add);
                            } catch (NumberFormatException e) {
                                messenger.print("Check users parameter");

                            }
                            task.setUserList(ulist);
                        }


                }
            }

        }
        // trigger new task in save method
        if (task.getId() == null) task.setId(-1);
        return task;
    }

    @Override
    public Optional<List<? extends StoreUnit>> execute(String args) {
        Matcher idMatcher = Pattern.compile("^task\\s+-id\\s+(\\d+)$").matcher(args.trim());
        Matcher addMatcher = Pattern.compile("^task\\s+-id\\s+(\\d+)\\s+--add-user\\s+(\\d+)").matcher(args.trim());
        Matcher removeMatcher = Pattern.compile("^task\\s+-id\\s+(\\d+)\\s+--remove-user\\s+(\\d+)").matcher(args.trim());
        Optional<List<? extends StoreUnit>> empty = Optional.empty();
        int id = -1;
        String command = getCommand(args, messenger);
        switch (command) {
            case "-a":
            case "--all":
                return Optional.of((List<Task>) repository.findAll());
            case "-d":
            case "--delete":
                id = getId(args, messenger);
                if (id != 0) {
                    repository.deleteById(id);
                    return empty;
                }
                break;
            case "--update":
                Task task = setTaskForUpdate(args);
                return Optional.of(List.of((Task)repository.save(task)));
            case "-id":
                id = getId(args, messenger);
                if (id == 0) {
                    return empty;
                }
                if (idMatcher.find()) {
                    Optional<Task> opti = repository.findById(id);
                    if (opti.isPresent()) {
                        return Optional.of(List.of(opti.get()));
                    }
                }
                if (addMatcher.find()) {
                    // isEmpty() here because it may be empty.
                    Optional<List<? extends StoreUnit>> result = processUserCommand(args, "--add", id);
                    //noinspection SimplifyOptionalCallChains
                    if (!result.isEmpty()) {
                        return result;
                    }
                }
                if (removeMatcher.find()) {
                    Optional<List<? extends StoreUnit>> result = processUserCommand(args, "--remove", id);
                    //noinspection SimplifyOptionalCallChains
                    if (!result.isEmpty()) {
                        return result;
                    }
                }
                break;
            case "-uid":
                Matcher matcher = Pattern.compile("(-uid)\\s+(\\d+)").matcher(args.trim());
                List<Task> res = new ArrayList<>();
                int searchId = -1;
                if (matcher.find()) {
                    List<Task> tasks = (List<Task>) repository.findAll();
                    try {
                        searchId = Integer.parseInt(matcher.group(2));
                        for (Task t : tasks) {
                            List<User> users = t.getUserList();
                            for (User u : users) {
                                if (u.getId() == searchId) {
                                    res.add(t);
                                    break;
                                }
                            }

                        }
                        return Optional.of(res);
                    } catch (NumberFormatException e) {
                        messenger.print("Check -uid parameter");
                    }

                }
                break;
        }
        return empty;
    }
}
