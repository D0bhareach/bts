package com.github.zxxz_ru.storage.file;

import com.github.zxxz_ru.entity.StoreUnit;
import com.github.zxxz_ru.storage.RepositoryCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

public class FileSystemRepositoryCreator implements RepositoryCreator {

    @Autowired
    UserFileRepository userFileRepository;
    @Autowired
    TaskFileRepository taskFileRepository;
    @Autowired
    ProjectFileRepository projectFileRepository;

    public FileSystemRepositoryCreator(){}

    @Override
    public UserFileRepository getUserRepository() {
        return userFileRepository;
    }

    @Override
    public TaskFileRepository getTaskRepository() {
        return taskFileRepository;
    }

    @Override
    public ProjectFileRepository getProjectRepository() {
        return projectFileRepository;
    }
}
