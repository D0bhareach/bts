package com.github.zxxz_ru.storage.file;

import com.github.zxxz_ru.entity.Project;
import com.github.zxxz_ru.entity.StoreUnit;
import com.github.zxxz_ru.entity.Task;
import com.github.zxxz_ru.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileSystemRepository<S extends StoreUnit> implements CrudRepository<S, Integer> {

    @Autowired
    private Storage storage;
    private List<S> list;
    private final EntityMode mode;

    @SuppressWarnings("unchecked")
    public FileSystemRepository(EntityMode mode) {
        this.mode = mode;
        switch (mode) {
            case USER:
                list = (List<S>) storage.getUsers();
                break;
            case TASK:
                list = (List<S>) storage.getTasks();
                break;
            case PROJECT:
                list = (List<S>) storage.getProjects();
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void updateStorage() {
        switch (mode) {
            case USER:
                storage.setUsers((List<User>) list);
            case TASK:
                storage.setTasks((List<Task>) list);
            case PROJECT:
                storage.setProjects((List<Project>) list);
        }
    }

    public long count() {
        return list.size();
    }

    public void delete(S entity) {
        if (list.contains(entity)) {
            list.remove(entity);
            updateStorage();
        }
    }

    @Override
    public void deleteAll(Iterable<? extends S> entities) {

    }

    public void deleteAll() {
        list = new ArrayList<>();
        updateStorage();

    }

    public void deleteAll(List<S> entities) {
        entities.forEach(this::delete);
        updateStorage();
    }

    public void deleteById(Integer id) {
        for (S e : list) {
            if (e.getId().equals(id)) {
                list.remove(e);
                updateStorage();
            }
        }
    }

    public boolean existsById(Integer id) {
        for (Object e : list) {
            StoreUnit ent = (StoreUnit) e;
            if (ent.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public Iterable<S> findAll() {
        return list;
    }

    public Iterable<S> findAllById(Iterable<Integer> ids) {
        ArrayList<S> res = new ArrayList<>();
        for (S e : list) {
            int id = ((StoreUnit) e).getId();
            for (int i : ids) {
                if (id == i) {
                    res.add(e);
                }
            }
        }
        return res;
    }


    public Optional<S> findById(Integer id) {
        S res = null;
        for (S e : list) {
            if (e.getId().equals(id)) {
                res = e;
            }
        }
        return Optional.ofNullable(res);
    }

    public <S1 extends S>S1 save(S1 entity) {
        int counter = entity.getId();
        if (counter <= 0) {
            switch (mode) {
                case USER:
                    counter = storage.getNextUserId();
                    break;
                case TASK:
                    counter = storage.getNextTaskId();
                    break;
                case PROJECT:
                    counter = storage.getNextProjectId();
                    break;
            }
            entity.setId(counter);
        }
        list.add(entity);
        updateStorage();
        return entity;
    }

    @Override
    public <S1 extends S> Iterable<S1> saveAll(Iterable<S1> entities) {
        for (S1 e : entities) save(e);
        return (List<S1>)list;
    }

}
