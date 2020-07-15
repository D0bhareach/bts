package com.github.zxxz_ru.command;

import com.github.zxxz_ru.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.zxxz_ru.entity.User;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
class UserCommand implements Commander {

    @Autowired
    private UserRepository re;
    private List<User> getAll(){
        @SuppressWarnings("unchecked")
        List result = (List<User>)re.findAll();
       return result;
    }
    private void deleteUser(int id){
        List<User> list = getUser(id);
        if(list.size() != 0) {
            re.delete(list.get(0));
        }
    }
    private List<User> getUser(int id){
        List<User> res = new ArrayList();
        @SuppressWarnings("unchecked")
        Optional<User> opt = re.findById(id);
        try {
            res.add(opt.get());
            return res;
        } catch (NoSuchElementException e) {
            return res;
        }
    }
    private void assignTask(int userId, int taskId){}
    private void dropTask(int userId, int taskId){}

    private String parseSingleParameter(String pair) throws NoSuchElementException{
    int index = pair.lastIndexOf("=");
        if(index<0) throw new NoSuchElementException("Incorrect usage [ = ] is missing.");
        String param = pair.substring(index);
        if(param.length() == 0) throw new NoSuchElementException("No Parameter after equality Character.");
        return param;
    }
    private boolean idIsPresent(String... args){
        return Stream.of(args).anyMatch((a)-> Pattern.matches("id(\\=){1}.*", a));
    }

    // TODO: putUser method not complete totally wrong make new.
    private List<User> putUser(String... data){
        List<User> list = new ArrayList();
        User u;
        int count = (int)re.count();
        if(data.length == 4){
           u = new User(count+1, data[1], data[2], data[3]);
        } else if(data.length == 3 ){
            u = new User(count+1, data[1], data[2], "Developer");
        }
        return list;
    }

    @Override
    public void execute(String... args){
        String z = args[0];
        switch(z){
            case "-a": case "--all":
                Util.print(getAll());

        }
        System.exit(0);
    }
}
