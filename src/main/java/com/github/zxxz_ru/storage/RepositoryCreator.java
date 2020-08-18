package com.github.zxxz_ru.storage;

import com.github.zxxz_ru.entity.StoreUnit;
import org.springframework.data.repository.CrudRepository;

public interface RepositoryCreator {
    CrudRepository<? extends StoreUnit, Integer> getUserRepository();
    CrudRepository<? extends StoreUnit, Integer> getTaskRepository();
    CrudRepository<? extends StoreUnit, Integer> getProjectRepository();
}
