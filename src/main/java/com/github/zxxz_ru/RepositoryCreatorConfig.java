package com.github.zxxz_ru;

import com.github.zxxz_ru.storage.dao.DatabaseRepositoryCreator;
import com.github.zxxz_ru.storage.file.FileSystemRepositoryCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryCreatorConfig {


    @Bean
    public FileSystemRepositoryCreator fileSystemRepositoryCreator() {
        return new FileSystemRepositoryCreator();
    }

    @Bean
    public DatabaseRepositoryCreator databaseRepositoryCreator() {
        return new DatabaseRepositoryCreator();
    }

}
