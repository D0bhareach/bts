package com.github.zxxz_ru;

import com.github.zxxz_ru.storage.file.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileSystemRepositoryConfig {
    @Autowired
    private Storage storage;

    @Bean
    public UserFileRepository userFileRepository() {
        return new UserFileRepository(storage, EntityMode.USER);
    }

    @Bean
    TaskFileRepository taskFileRepository() {
        return new TaskFileRepository(storage, EntityMode.TASK);
    }

    @Bean
    ProjectFileRepository projectFileRepository() {
        return new ProjectFileRepository(storage, EntityMode.PROJECT);
    }
}
