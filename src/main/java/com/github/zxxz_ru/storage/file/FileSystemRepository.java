package com.github.zxxz_ru.storage.file;

import com.github.zxxz_ru.entity.StoreUnit;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("NullableProblems")
public class FileSystemRepository<S extends StoreUnit> implements CrudRepository<S, Integer> {

   //  private final Messenger messenger;
    private final Storage storage;
    private List<S> list;
    private final EntityMode mode;

/*
    public FileSystemRepository setList(List<S> list) {
        this.list = list;
        return this;
    }

    public FileSystemRepository setMode(EntityMode mode) {
        this.mode = mode;
        return this;
    }

 */

    // public FileSystemRepository(){}
    public FileSystemRepository(Storage storage, EntityMode mode) {
        this.storage = storage;
        // this.messenger = messenger;
        this.list = (List<S>) storage.getList(mode);
        this.mode = mode;
    }

    private void updateStorage() {
        storage.updateStorageList(list, mode);
    }

    @Override
    public long count() {
        return list.size();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void delete(S entity) {
        if (list.contains(entity)) {
            list.remove(entity);
            updateStorage();
        }
    }

    @Override
    public void deleteAll(Iterable<? extends S> entities) {
        for (S s : entities) {
            delete(s);
        }
    }

    @Override
    public void deleteAll() {
        list = new ArrayList<>();
        updateStorage();

    }


    @Override
    public void deleteById(Integer id) {
        Optional<S> opti = findById(id);
        if (opti.isPresent()) {
            S s = opti.get();
            list.remove(s);
        }
    }

    @Override
    public boolean existsById(Integer id) {
        for (S e : list) {
            if (e.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterable<S> findAll() {
        return list;
    }

    @Override
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


    @Override
    public Optional<S> findById(Integer id) {
        Optional<S> opti = Optional.empty();
        for (S e : list) {
            if (e.getId().equals(id)) {
                opti = Optional.of(e);
            }
        }
        return opti;
    }

    private int getNextCounter() {
        int id = -1;
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
        return id;
    }

    private int getNextCounter(int id) {
        int counter;
        switch (mode) {
            case USER:
                counter = storage.getUserCounter();
                if (id >= counter + 1) storage.setUserCounter(id);
                break;
            case TASK:
                counter = storage.getTaskCounter();
                if (id >= counter + 1) storage.setTaskCounter(id);
                break;
            case PROJECT:
                counter = storage.getProjectCounter();
                if (id >= counter + 1) storage.setProjectCounter(id);
                break;
        }
        return id;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public <S1 extends S> S1 save(S1 entity) {
        int id = entity.getId();
        // add new Entity
        if (id <= 0) {
            entity.setId(getNextCounter());
            list.add(entity);
        } else {
            // must replace
            int finalId = id;
            int idx;
            List<S> filtered = list.stream().filter(e -> e.getId() == finalId).collect(Collectors.toList());
            try {
                idx = list.indexOf(filtered.get(0));
            } catch (IndexOutOfBoundsException e) {
                idx = -1;
            }
            if (idx >= 0) {
                S e = list.get(idx);
                e = e.from(entity);
                list.set(idx, e);
            } else {
                id = getNextCounter(id);
                list.add(entity);
            }
        }
        updateStorage();
        return entity;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <S1 extends S> Iterable<S1> saveAll(Iterable<S1> entities) {
        for (S1 e : entities) save(e);
        return (List<S1>) list;
    }

}
