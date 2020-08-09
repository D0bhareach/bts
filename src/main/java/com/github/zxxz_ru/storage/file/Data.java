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
    List<List<? extends StoreUnit>> data = new ArrayList<>();
    AtomicInteger userCounter = new AtomicInteger(0);
    AtomicInteger taskCounter = new AtomicInteger(0);
    AtomicInteger projectCounter = new AtomicInteger(0);

    public void clear(){
        this.data = new ArrayList<>();
    }

    public List<List<? extends StoreUnit>> getData() {
        return this.data;
    }

    public void setData(List<List<? extends StoreUnit>> d) {
        this.data = d;
    }
    // Methods to get specific List from
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        if (data.size() == 3) {
                return (List<User>) data.get(2);
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public List<Task> getTasks() {
        if (data.size() == 3) {
            return (List<Task>) data.get(1);
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public List<Project> getProjects() {
        if (data.size() == 3) {
            return (List<Project>) data.get(0);
        }
        return new ArrayList<>();
    }

    // Methods to set specific List of Entities
    public void setProjects(List<Project> projects) {
        this.data.set(0, projects);
    }

    public void setTasks(List<Task> tasks) {
        this.data.set(1, tasks);
    }

    public void setUsers(List<User> users) {
        this.data.set(2, users);
    }


}
