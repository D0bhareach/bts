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

public class FileSystemRepository implements CrudRepository<StoreUnit, Integer> {

    @Autowired
    private  Storage storage;
    private List list;
    private EntityMode mode;

    @SuppressWarnings("unchecked")
    public FileSystemRepository(EntityMode mode) {
        this.mode = mode;
        switch (mode){
            case USER:
                list = (List<User>)storage.getUsers();
            case TASK:
                list = (List<Task>) storage.getTasks();
            case PROJECT:
                list = (List<Project>) storage.getProjects();
        }

    }


    private void updateStorage(){
        switch (mode){
            case USER:
                storage.setUsers(list);
            case TASK:
                storage.setTasks(list);
            case PROJECT:
                storage.setProjects(list);
        }
    }

    public long	count(){return  list.size();}
    public void	delete(StoreUnit entity){
        if(list.contains(entity)){
            list.remove(entity);
            updateStorage();
        }
    }
    public void	deleteAll(){
        list = new ArrayList();
        updateStorage();

    }
    public void	deleteAll(Iterable<? extends StoreUnit> entities){
        entities.forEach(e ->{
            delete(e);
        });
        updateStorage();
    }
    public void	deleteById(Integer id){
        for(Object e : list){
            StoreUnit ent = (StoreUnit)e;
            if(ent.getId().equals(id)){
                list.remove(e);
                updateStorage();
            }
        }
    }
    public boolean	existsById(Integer id){
        for( Object e : list){
            StoreUnit ent = (StoreUnit) e;
            if(ent.getId().equals(id)){
                return true;
            }
                }

        return false;
    }
    public Iterable<StoreUnit>	findAll(){
        return list;
    }

    public Iterable <StoreUnit> findAllById(Iterable<Integer> ids) {
        ArrayList<StoreUnit> res = new ArrayList();
        for (Object e : list) {
            StoreUnit ent = (StoreUnit) e;
            Integer id = ent.getId();
            for (Integer i : ids) {
                if (id.equals(i)) {
                    res.add(ent);
                }
            }
        }
        return res;
    }

    public Optional<StoreUnit> findById(Integer id){
        StoreUnit res = null;
        for(Object o : list){
            StoreUnit e = (StoreUnit) o;
            if(e.getId().equals(id)){
                res = e;
            }
        }
        return Optional.ofNullable(res);
    }

    public <S extends StoreUnit> S	save(S entity){
        int idx = list.indexOf(entity);
        if(idx >=0){
            list.set(idx, entity);

        } else {
            list.add(entity);
        }
        updateStorage();
        return entity;
    }

    public <S extends StoreUnit>Iterable<S>	saveAll(Iterable<S> entities){
        entities.forEach(e->{save(e);});
        updateStorage();
        return list;
    }

}
