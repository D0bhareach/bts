package com.github.zxxz_ru.storage.dao;

import com.github.zxxz_ru.entity.StoreUnit;
import com.github.zxxz_ru.storage.RepositoryCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;


public class DatabaseRepositoryCreator implements RepositoryCreator {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    ProjectRepository projectRepository;

    public DatabaseRepositoryCreator(){}
    public UserRepository getUserRepository(){
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
