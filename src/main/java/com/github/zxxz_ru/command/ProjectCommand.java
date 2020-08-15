package com.github.zxxz_ru.command;

import com.github.zxxz_ru.entity.Project;
import com.github.zxxz_ru.entity.Task;
import com.github.zxxz_ru.storage.file.EntityMode;
import com.github.zxxz_ru.storage.file.FileSystemRepository;
import com.github.zxxz_ru.storage.file.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class ProjectCommand implements Commander<Project> {
    private final Messenger messenger;
    private final FileSystemRepository<Project> repository;
    private final Storage storage;

    @Autowired
    public ProjectCommand(Storage storage, Messenger messenger) {
        this.storage = storage;
        this.messenger = messenger;
        repository = new FileSystemRepository<>(storage, messenger, storage.getProjects(), EntityMode.PROJECT);
    }

    private Optional<List<Project>> processTaskCommand(String args, String prefix, int id) {
        Optional<List<Project>> empty = Optional.empty();
        Optional<List<Project>> result = Optional.empty();
        int taskId = 0;
        String pattern = new StringBuilder(prefix).append("-task\\s+(\\d+)").substring(0);
        Pattern p1 = Pattern.compile(pattern);

        Matcher m1 = p1.matcher(args);
        if (m1.find()) {
            String IdString = m1.group(1);
            try {
                taskId = Integer.parseInt(IdString.trim());
            } catch (NumberFormatException e) {
                messenger.print("Check task id value.");
                return empty;
            }
            FileSystemRepository<Task> taskRepository =
                    new FileSystemRepository<>(storage, messenger, storage.getTasks(), EntityMode.TASK);
            Optional<Task> opti = taskRepository.findById(taskId);
            if (opti.isPresent()) {
                List<Project> projects = storage.getProjects();
                Optional<Project> optiOld = repository.findById(id);
                if (prefix.equals("--add")) {
                    if (optiOld.isPresent()) {
                        Project newProject = new Project();
                        newProject.setTaskList(List.of(opti.get()));
                        Project old = optiOld.get();
                        int index = projects.indexOf(old);
                        old = old.from(newProject);
                        result = Optional.of(List.of(old));
                        projects.set(index, old);
                    }
                    storage.setProjects(projects);
                    return result;
                } else if (prefix.equals("--remove")) {
                    if (optiOld.isPresent()) {
                        Project old = optiOld.get();
                        int index = projects.indexOf(old);
                        List<Task> tasks = old.getTaskList();
                        tasks.remove(opti.get());
                        old.setTaskList(tasks);
                        projects.set(index, old);
                        result = Optional.of(List.of(old));
                    }
                }
                storage.setProjects(projects);
                return result;
            }
        }
        return empty;
    }

    private Project setProjectForUpdate(String args) {
        List<String> parameters = List.of("id", "name", "description", "tasks");
        Project project = new Project();
        for (String parameter : parameters) {
            Pattern pattern = preparePattern(parameter);
            Matcher matcher = pattern.matcher(args);
            if (matcher.find()) {
                switch (parameter) {
                    case "id":
                        String id = matcher.group(3);
                        if (id != null) {
                            project.setId(Integer.parseInt(id));
                        } else {
                            // in save method it will trigger new User
                            project.setId(-1);
                        }
                        break;
                    case "name":
                        project.setProjectName(matcher.group(3));
                        break;
                    case "description":
                        project.setDescription(matcher.group(3));
                        break;
                    case "tasks":
                        List<Task> list = new ArrayList<>();
                        FileSystemRepository<Task> taskRepository = new FileSystemRepository<>(
                                storage, messenger, storage.getTasks(), EntityMode.TASK
                        );
                        String ids = matcher.group(2);
                        String[] tids = ids.split(",");
                        for (String s : tids) {
                            try {
                                int taskId = Integer.parseInt(s);
                                Optional<Task> opti = taskRepository.findById(taskId);
                                opti.ifPresent(list::add);
                            } catch (NumberFormatException e) {
                                messenger.print("Check users parameter");

                            }
                            project.setTaskList(list);
                        }


                }
            }

        }
        // trigger new task in save method
        if (project.getId() == null) project.setId(-1);
        return project;
    }

    @Override
    public Optional<List<Project>> execute(String args) {
        Optional<List<Project>> empty = Optional.empty();
        int id = -1;
        String command = getCommand(args, messenger);
        switch (command) {
            case "-a":
            case "--all":
                return Optional.of(((List<Project>) repository.findAll()));
            case "-d":
            case "--delete":
                id = getId(args, messenger);
                if (id != 0) {
                    repository.deleteById(id);
                    return  empty;
                }
                break;
            case "--update":
                Project p = setProjectForUpdate(args);
                return Optional.of(List.of(repository.save(p)));
            case "-id":
                id = getId(args, messenger);
                Matcher mtchr = Pattern.compile("^project\\s+-id\\s+(\\d+)$").matcher(args.trim());
                if (mtchr.find()) {
                    Optional<Project> opti = repository.findById(id);
                    if(opti.isPresent()) {
                        return Optional.of(List.of(opti.get()));
                    }
                    }
                Optional<List<Project>> res = processTaskCommand(args, "--add", id);
                if (!res.isEmpty()) {
                    return res;
                }
                res = processTaskCommand(args, "--remove", id);
                if (!res.isEmpty()) {
                    return res;
                }
                break;
        }
        return empty;
    }

}