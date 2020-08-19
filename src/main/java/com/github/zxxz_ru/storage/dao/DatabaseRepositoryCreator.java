package com.github.zxxz_ru.storage.dao;

import com.github.zxxz_ru.storage.RepositoryCreator;
import org.springframework.beans.factory.annotation.Autowired;


public class DatabaseRepositoryCreator implements RepositoryCreator {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    ProjectRepository projectRepository;

    public DatabaseRepositoryCreator() {
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    @Override
    public ProjectRepository getProjectRepository() {
        return projectRepository;
    }
}
