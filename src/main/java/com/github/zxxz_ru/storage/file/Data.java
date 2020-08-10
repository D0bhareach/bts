package com.github.zxxz_ru.storage.file;

import com.github.zxxz_ru.entity.Project;
import com.github.zxxz_ru.entity.StoreUnit;
import com.github.zxxz_ru.entity.Task;
import com.github.zxxz_ru.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class to hold App's data
 */
public class Data {
    List<User> users;
    List<Task> tasks;
    List<Project> projects;
    AtomicInteger userCounter;
    AtomicInteger taskCounter;
    AtomicInteger projectCounter;

    public Data(){
        clear();
        userCounter = new AtomicInteger(0);
        taskCounter = new AtomicInteger(0);
        projectCounter = new AtomicInteger(0);
    }

    public final void clear(){
        projects = new ArrayList<Project>();
        tasks = new ArrayList<Task>();
        users = new ArrayList<User>();
    }


    public List<User> getUsers(){
        return users;
    }

    @SuppressWarnings("unchecked")
    public List<Task> getTasks() {
        return tasks;
    }

    @SuppressWarnings("unchecked")
    public List<Project> getProjects() {
        return projects;
    }

    // Methods to set specific List of Entities
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public AtomicInteger getUserCounter() {
        return userCounter;
    }

    public void setUserCounter(AtomicInteger userCounter) {
        this.userCounter = userCounter;
    }

    public AtomicInteger getTaskCounter() {
        return taskCounter;
    }

    public void setTaskCounter(AtomicInteger taskCounter) {
        this.taskCounter = taskCounter;
    }

    public AtomicInteger getProjectCounter() {
        return projectCounter;
    }

    public void setProjectCounter(AtomicInteger projectCounter) {
        this.projectCounter = projectCounter;
    }
}
