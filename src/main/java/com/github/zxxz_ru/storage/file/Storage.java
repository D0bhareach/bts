package com.github.zxxz_ru.storage.file;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.zxxz_ru.command.Messenger;
import com.github.zxxz_ru.entity.Project;
import com.github.zxxz_ru.entity.StoreUnit;
import com.github.zxxz_ru.entity.Task;
import com.github.zxxz_ru.entity.User;
import com.github.zxxz_ru.storage.InitialDataInserter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Storage Class is utility class for all File Repositories Classes.
 * It should load all ArrayLists of Objects  from File System.
 * It should keep cached data at all the time and immediately before application exits
 * Storage Class will write data back to File System.
 * It it must have methods to return each ArrayList of Entities and method to replace ArrayList of
 * Entity when List has changed.
 */
@Component
public class Storage {
    @Autowired
    private InitialDataInserter inserter;

    /**
     * Path to Storage File. Path is either default value or can be set from
     * Application Parameters.
     */
    private final File file;
    private final Messenger messenger;
    private final StorageFileCreator creator;
    private final ObjectMapper mapper = new ObjectMapper().
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final Data data;

    // Instead of elaborate creation and insertion just need to set data.
    @Autowired
    public Storage(Messenger messenger, StorageFileCreator creator) {
        this.creator = creator;
        this.messenger = messenger;
        file = creator.createStorageFile();
        if (file.length() <= 0) {
            if (inserter != null) {
                data = inserter.getData();
            } else {
                data = new Data();
            }
            this.writeData();
        } else {
            this.data = readData();
        }
    }

    // Since I do not want use map must keep inner Lists order constant
    // data[0] - return ArrayList of Projects
    // data[1] - return ArrayList of Tasks
    // data[2] - return ArrayList of Users


    public void writeData() {
        try {
            ObjectWriter writer = mapper.writerFor(Data.class);
            writer.writeValue(file, data);
            // mapper.writeValue(file, data);
        } catch (IOException e) {
            messenger.print("Error writing data to: " + file.getAbsolutePath());
            System.exit(1);
        }
    }


    private Data readData() {
        if (!file.exists()) {
            messenger.print("Cannot read Storage File. File not exists.");
            return null;
        }
        if (file.length() > 0) {
            try {
                return mapper.readValue(file, Data.class);
            } catch (IOException e) {
                e.printStackTrace();
                messenger.print("Error reading Data from: " + file.getPath());
                System.exit(1);
            }
        }
        return new Data();
    }

    // Methods to get specific List from
    public List<User> getUsers() {
        return data.getUsers();
    }

    public List<Task> getTasks() {
        return data.getTasks();
    }

    public List<Project> getProjects() {
        return data.getProjects();
    }

    // Methods to set specific List of Entities
    public void setProjects(List<Project> projects) {
        this.data.setProjects(projects);
    }

    public void setTasks(List<Task> tasks) {
        this.data.setTasks(tasks);
    }

    public void setUsers(List<User> users) {
        this.data.setUsers(users);
    }

    /**
     * @return next user id.
     */
    public int getNextProjectId() {
        return data.projectCounter.addAndGet(1);
    }

    public int getNextTaskId() {
        return data.taskCounter.addAndGet(1);
    }

    public int getNextUserId() {
        return data.userCounter.addAndGet(1);
    }

    // need this methods in FileSystemRepository save method for cases
    // when user --update id is bigger than counter + 1.
    public int getProjectCounter() {
        return data.projectCounter.get();
    }

    public int getTaskCounter() {
        return data.taskCounter.get();
    }

    public int getUserCounter() {
        return data.userCounter.get();
    }

    // need this methods to set counters for Entities in data. User in save
    // method of FileSystemRepository in cases when --update command pass
    // id value bigger than counter + 1

    public void setProjectCounter(int i) {
        data.setProjectCounter(new AtomicInteger(i));
    }

    public void setTaskCounter(int i) {
        data.setTaskCounter(new AtomicInteger(i));
    }

    public void setUserCounter(int i) {
        data.setUserCounter(new AtomicInteger(i));
    }

    public List<? extends StoreUnit> getList(EntityMode mode) {
        switch (mode) {
            case USER:
                return this.getUsers();
            case TASK:
                return this.getTasks();
            case PROJECT:
                return this.getProjects();
        }
        return List.of();
    }

    @SuppressWarnings("unchecked")
    public <T extends StoreUnit> void updateStorageList(List<T> list, EntityMode mode) {
        switch (mode) {
            case USER:
                this.setUsers((List<User>) list);
                break;
            case TASK:
                this.setTasks((List<Task>) list);
                break;
            case PROJECT:
                this.setProjects((List<Project>) list);
                break;
        }
    }
}
