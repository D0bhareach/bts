package com.github.zxxz_ru.storage.file;

import com.github.zxxz_ru.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserFileRepository extends CrudRepository<User, Integer> {
}
