package com.github.zxxz_ru.dao;

import com.github.zxxz_ru.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}