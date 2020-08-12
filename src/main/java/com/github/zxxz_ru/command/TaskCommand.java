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
    private Messenger messenger;
    private FileSystemRepository repository;

    @Autowired
    public TaskCommand(Storage storage, Messenger messenger) {
        this.messenger = messenger;
        repository = new FileSystemRepository<Task>(storage, messenger, storage.getTasks(), EntityMode.TASK);
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
                /*
                if (processTaskCommand(args, "--assign", id)) {
                    break;
                } else if (processTaskCommand(args, "--drop", id)) {
                    break;
                } else {
                    break;
                }

                 */
        }
    }
}
