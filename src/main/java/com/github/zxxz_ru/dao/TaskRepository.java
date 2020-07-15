package com.github.zxxz_ru.dao;

import com.github.zxxz_ru.entity.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Integer> {
}
