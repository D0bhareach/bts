package com.github.zxxz_ru.command;

import com.github.zxxz_ru.entity.Project;
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
class ProjectCommand implements Commander {
    private final Messenger messenger;
    private final FileSystemRepository<Project> repository;
    private final Storage storage;

    @Autowired
    public ProjectCommand(Storage storage, Messenger messenger) {
        this.storage = storage;
        this.messenger = messenger;
        repository = new FileSystemRepository<Project>(storage, messenger, storage.getProjects(), EntityMode.PROJECT);
    }

    @Override
    public void execute(String args) {
        int id = -1;
        String command = getCommand(args, messenger);
        switch (command) {
            case "-a":
            case "--all":
                messenger.print((List<Project>) repository.findAll());
                break;
            case "-d":
            case "--delete":
                id = getId(args, messenger);
                if (id != 0) {
                    repository.deleteById(id);
                }
                break;
            case "--update":
                // Task task = setTaskForUpdate(args);
                // repository.save(task);
            case "-id":
                id = getId(args, messenger);
                Matcher mtchr = Pattern.compile("^project\\s+-id\\s+(\\d+)$").matcher(args.trim());
                if (mtchr.find()) {
                    Optional<Project> opti = repository.findById(id);
                    opti.ifPresent(project -> messenger.print(List.of(project)));
                    break;
                }
        }
    }

}