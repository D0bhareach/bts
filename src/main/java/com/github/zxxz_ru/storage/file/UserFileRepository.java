package com.github.zxxz_ru.storage.file;

import com.github.zxxz_ru.entity.User;

public class UserFileRepository extends FileSystemRepository<User>{
    public UserFileRepository(Storage storage, EntityMode mode){
        super(storage, mode);
    }
}
