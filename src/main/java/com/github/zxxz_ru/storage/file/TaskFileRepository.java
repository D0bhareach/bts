package com.github.zxxz_ru.storage.file;

import com.github.zxxz_ru.entity.Task;

public class TaskFileRepository extends FileSystemRepository<Task> {
    public TaskFileRepository(Storage storage, EntityMode mode) {
        super(storage, mode);
    }
}
