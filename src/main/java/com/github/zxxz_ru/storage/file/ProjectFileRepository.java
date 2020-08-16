package com.github.zxxz_ru.storage.file;

import com.github.zxxz_ru.entity.Project;

public class ProjectFileRepository extends FileSystemRepository<Project> {
    public ProjectFileRepository(Storage storage, EntityMode mode) {
        super(storage, mode);
    }
}
