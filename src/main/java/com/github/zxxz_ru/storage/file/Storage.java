package com.github.zxxz_ru.storage.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zxxz_ru.AppState;
import com.github.zxxz_ru.command.Messenger;
import com.github.zxxz_ru.entity.Project;
import com.github.zxxz_ru.entity.Task;
import com.github.zxxz_ru.entity.User;
import com.github.zxxz_ru.storage.InitialDataInserter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Storage Class is utility class for all File Repositories Classes.
 * It should load all ArrayLists of Objects  from File System.
 * It should keep cached data at all the time and immediately before application exits
 * Storage Class will write data back to File System.
 * It it must have methods to return each ArrayList of Entities and method to replace ArrayList of
 * Entity when List has changed.
 *
 */
@Component
public class Storage {
    // enum Mode {USER, TASK, PROJECT}

    ;
    /**
     * Path to Storage File. Path is either default value or can be set from
     * Application Parameters.
     */
    private File file;
    private final Messenger messenger;
    private final AppState state;
    private final StorageFileCreator creator;
    private final ObjectMapper mapper = new ObjectMapper().
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private Data data;

    @Autowired
    public Storage(AppState state, Messenger messenger, StorageFileCreator creator, InitialDataInserter inserter) {
        this.creator = creator;
        this.state = state;
        this.messenger = messenger;
        file = creator.createStorageFile();
        // insert data in file change it when find how to use profiles.
        if(file.length() <= 0){
            List users = inserter.createUserList();
            List tasks = inserter.createTaskList(users);
            List projects = inserter.createProjectList(tasks);
            this.data = new Data();
            this.data.setUsers(users);
            this.data.setUserCounter(new AtomicInteger(users.size()));
            this.data.setTasks(tasks);
            this.data.setTaskCounter(new AtomicInteger(tasks.size()));
            this.data.setProjects(projects);
            this.data.setProjectCounter(new AtomicInteger(projects.size()));
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
            mapper.writeValue(file, data);
        } catch (IOException e) {
            messenger.printError("Error writing data to: " + file.getAbsolutePath());
            System.exit(1);
        }
    }


    private Data readData() {
        if (!file.exists()) {
            messenger.printError("Cannot read Storage File. File not exists.");
            return null;
        }
        if (file.length() > 0) {
            try {
                return  mapper.readValue(file, new TypeReference<>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
                messenger.printError("Error reading Data from: " + file.getPath());
                System.exit(1);
            }
        }
        return  new Data();
    }

    // Methods to get specific List from
    public List<User> getUsers() {
            return data.getUsers();
    }

    public List<Task> getTasks() {
        return data.getTasks();
    }

    public List<Project> getProjects() {
        return getProjects();
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

    public int getNextProjectId(){
        return data.projectCounter.addAndGet(1);
    }
    public int getNextTaskId(){
        return data.taskCounter.addAndGet(1);
    }
    public int getNextUserId(){
        messenger.print(4, "USER COUNTER IN STORGE:" + data.getUserCounter().get());
        return data.userCounter.addAndGet(1);
    }



    /*
    public void store(List<T> list){
        final String storageDir = "~/opt/storage/epam/";
        final String userFile = storageDir+"users";
        final String taskFile = storageDir+"tasks";
        final String projectFile = storageDir+"projects";
        try(
            FileOutputStream fileOut =
                    new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            ){
         out.writeObject(list);
      }  catch (IOException i) {
         i.printStackTrace();
      }
    }

    @SuppressWarnings("unchecked")
    public List<T> get(Mode mode){
        List<T> list = new ArrayList<>();
        try (
                FileInputStream fileIn = new FileInputStream(path);
                ObjectInputStream in = new ObjectInputStream(fileIn)){
             list = (List<T>) in.readObject();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
        }
        return list;
    }

     */
}
