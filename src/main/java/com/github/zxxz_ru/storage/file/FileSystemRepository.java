package com.github.zxxz_ru.storage.file;

import com.github.zxxz_ru.command.Messenger;
import com.github.zxxz_ru.entity.Project;
import com.github.zxxz_ru.entity.StoreUnit;
import com.github.zxxz_ru.entity.Task;
import com.github.zxxz_ru.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileSystemRepository<S extends StoreUnit> implements CrudRepository<S, Integer> {

    private Messenger messenger;
    private Storage storage;
    private List<S> list;
    private EntityMode mode;

    public List<S> getList() {
        return list;
    }

    public FileSystemRepository setList(List<S> list) {
        this.list = list;
        return this;
    }
    public FileSystemRepository setMode(EntityMode mode){
        this.mode = mode;
        return this;
    }

    // public FileSystemRepository(){}
    public FileSystemRepository(Storage storage, Messenger messenger, List list, EntityMode mode) {
        this.storage =storage;
        this.messenger = messenger;
        this.list = list;
        this.mode = mode;
        /*
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

 */
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
        Optional<S> opti = findById(id);
        if(opti.isPresent()) {
            S s = opti.get();
            list.remove(s);
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
            int id = e.getId();
            for (int i : ids) {
                if (id == i) {
                    res.add(e);
                }
            }
        }
        return res;
    }


    public Optional<S> findById(Integer id) {
        Optional<S> opti = Optional.empty();
        S res = null;
        for (S e : list) {
            if (e.getId().equals(id)) {
                opti = Optional.of(e);
            }
        }
        return opti;
    }

    public <S1 extends S>S1 save(S1 entity) {
            int id = entity.getId();
        // add new Entity
        if (id <= 0) {
            switch (mode) {
                case USER:
                    id = storage.getNextUserId();
                    break;
                case TASK:
                    id = storage.getNextTaskId();
                    break;
                case PROJECT:
                    id = storage.getNextProjectId();
                    break;
            }
            entity.setId(id);
            list.add(entity);
        }else {
            // must replace
            int finalId = id;
            int idx = 0;
            List<S> filtered = list.stream().filter(e->e.getId() == finalId).collect(Collectors.toList());
            try {
                idx = list.indexOf(filtered.get(0));
            } catch (IndexOutOfBoundsException e){
                idx = -1;
            }
            if(idx>=0){
                S e = list.get(idx);
                e =  e.from(entity);
                list.set(idx, e);
            } else {
                list.add(entity);
            }
        }
        updateStorage();
        return entity;
    }

    @Override
    public <S1 extends S> Iterable<S1> saveAll(Iterable<S1> entities) {
        for (S1 e : entities) save(e);
        return (List<S1>)list;
    }

}
