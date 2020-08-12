package com.github.zxxz_ru.command;

import com.github.zxxz_ru.entity.Task;
import com.github.zxxz_ru.entity.User;
import com.github.zxxz_ru.storage.file.EntityMode;
import com.github.zxxz_ru.storage.file.FileSystemRepository;
import com.github.zxxz_ru.storage.file.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class TaskCommand implements Commander {
    private final Messenger messenger;
    private final FileSystemRepository repository;
    private final Storage storage;

    @Autowired
    public TaskCommand(Storage storage, Messenger messenger) {
        this.storage = storage;
        this.messenger = messenger;
        repository = new FileSystemRepository<Task>(storage, messenger, storage.getTasks(), EntityMode.TASK);
    }

    private void addUser(int id) {
    }

    private void deleteUser(int id) {
    }

    /**
     * @param args   command line
     * @param prefix prefix to regexp for finding task part in user's command line
     * @param id     id extracted from user's command line
     * @return true if all goes well, false otherwise
     */
    private boolean processUserCommand(String args, String prefix, int id) {
        int userId = 0;
        String pattern = new StringBuilder(prefix).append("-user\\s+(\\d+)").substring(0);
        Pattern p1 = Pattern.compile(pattern);

        Matcher m1 = p1.matcher(args);
        if (m1.find()) {
            String IdString = m1.group(1);
            try {
                userId = Integer.parseInt(IdString.trim());
            } catch (NumberFormatException e) {
                messenger.print(4, "Check task id value.");
                return false;
            }
            FileSystemRepository<User> userRepository =
                    new FileSystemRepository<User>(storage, messenger, storage.getUsers(), EntityMode.USER);
            Optional<User> opti = userRepository.findById(userId);
            if (opti.isPresent()) {
                List<Task> tasks = storage.getTasks();
                Optional<Task> optiOld = repository.findById(id);
                if (prefix.equals("--add")) {
                    if (optiOld.isPresent()) {
                        Task newTask = new Task();
                        newTask.setUserList(List.of(opti.get()));
                        Task old = optiOld.get();
                        int indx = tasks.indexOf(old);
                        old = old.from(newTask);
                        tasks.set(indx, old);
                    }
                    storage.setTasks(tasks);
                    return true;
                } else if (prefix.equals("--remove")) {
                    if (optiOld.isPresent()) {
                        Task old = optiOld.get();
                        int indx = tasks.indexOf(old);
                        List<User> users = old.getUserList();
                        users.remove(opti.get());
                        old.setUserList(users);
                        tasks.set(indx, old);
                    }
                }
                storage.setTasks(tasks);
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(String args) {
        int id = -1;
        String command = getCommand(args, messenger);
        switch (command) {
            case "-a":
            case "--all":
                messenger.print((List<User>) repository.findAll());
                break;
            case "-d":
                id = getId(args, messenger);
                if (id != 0) {
                    repository.deleteById(id);
                }
                break;
            case "--update":
                //    User user = setUserForUpdate(args);
                //  repository.save(user);
            case "-id":
                id = getId(args, messenger);
                Matcher mtchr = Pattern.compile("^task\\s+-id\\s+(\\d+)$").matcher(args.trim());
                if (mtchr.find()) {
                    Optional opti = repository.findById(id);
                    if (opti.isPresent()) {
                        messenger.print(List.of(opti.get()));
                    }
                    break;
                }
                if (processUserCommand(args, "--add", id)) {
                    break;
                } else if (processUserCommand(args, "--remove", id)) {
                    break;
                }
                break;
            case "-uid":
                messenger.print(4, "Got to uid command");

        }
    }
}
