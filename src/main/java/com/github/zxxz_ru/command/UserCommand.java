package com.github.zxxz_ru.command;

import com.github.zxxz_ru.storage.file.EntityMode;
import com.github.zxxz_ru.storage.file.FileSystemRepository;
import com.github.zxxz_ru.storage.file.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.zxxz_ru.entity.User;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class UserCommand implements Commander {

    private FileSystemRepository<User> repository;
    private Messenger messenger;

    @Autowired
    public UserCommand(Storage storage, Messenger messenger){
        this.messenger = messenger;
        repository = new FileSystemRepository<User>(storage, messenger, storage.getUsers(), EntityMode.USER);
    }

    public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    public void deleteAll(Iterable<? extends User> entities) {

    }

    private List<User> getAll() {
        return (List<User>) repository.findAll();
    }


    private List<User> getUser(int id) throws NoSuchElementException {
        List<User> res = new ArrayList<>();
        Optional<User> opt = repository.findById(id);
        opt.ifPresent(user -> res.add((User) user));
        return res;
    }

    private List<User> deleteUser(int id) {
        List<User> list = getUser(id);
        if (list.size() != 0) {
            repository.delete(list.get(0));
        }
        return list;
    }

    private void assignTask(int userId, int taskId) {
    }

    private void dropTask(int userId, int taskId) {
    }
/*
    private String getRoleParameter(String... args) throws NoSuchElementException {
        if (Arrays.stream(args).anyMatch(s -> Pattern.matches("role(=){1}.*", s))) {
            return util.getParameter("role", args);
        }
        return "Developer";
    }

 */

    private boolean isPresent(String param, String... args) {
        String rex = param + "(=){1}.*";
        return Arrays.stream(args).anyMatch((a) -> Pattern.matches(rex, a));
    }
/*
    private User createNewUser(String... args) throws IllegalArgumentException, NoSuchElementException {
        if (!isPresent("firstname", args))
            throw new IllegalArgumentException("user --put firstname is required parameter to create new  user.");
        if (!isPresent("lastname", args))
            throw new IllegalArgumentException("user --put lastname is required parameter to create new  user.");
        User user = new User();
        String firstName = util.getParameter("firstname", args);
        String lastName = util.getParameter("lastname", args);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        String role = getRoleParameter(args);
        user.setRole(role);
        return user;
    }

 */

    // Update method only one required parameter id.
    // Must get user from Db if not print message that user do not exists.
    // return updated user for reference.
    /*
    private List<User> updateUser(String... args) throws NoSuchElementException {
        List<User> list = new ArrayList<>();
        User user = new User();
        // no more fun!
        if (!isPresent("id", args)) messenger.printError("user --put id is required parameter.");
        int userId;
        // get userId from parameters or create new.
        userId = Integer.parseInt(util.getParameter("id", args));
        Optional<User> userOption = repository.findById(userId);
        if (userOption.isEmpty()) {
            messenger.printMessage("No user with id: " + userId);
        }
        user = userOption.get();
        if (isPresent("firstname", args)) {
            String firstName = util.getParameter("firstname", args);
            user.setFirstName(firstName);
        }
        if (isPresent("lastname", args)) {
            String lastName = util.getParameter("lastname", args);
            user.setLastName(lastName);
        }
        // Role Parameter not null and will be either default value or value provided in parameters.
        String role = getRoleParameter(args);
        user.setRole(role);
        user = (User) repository.save(user);
        list.add(user);
        return list;
    }

     */
/*
    // TODO: When inserting without id id is null!!!!
    private List<User> saveUser(String... args) throws NoSuchElementException, IllegalArgumentException {
        List<User> list = new ArrayList<>();
        User user = createNewUser(args);
        list.add(user);
        return list;
    }

 */

    /**
     * get parameters from arguments, create new user and set fields to argument's values
     * Does not set id if it's not provided, affectively new user
     * FileSystemRepository save(S entity) method will check for existence and availability if
     * id in saved Entities.
     * @param args command line
     * @return
     */
    private User setUserForUpdate(String args){
        List<String> parameters = List.of("id","firstname","lastname","role");
        User user = new User();
        for (String parameter : parameters){
            Pattern pattern= preparePattern(parameter);
            Matcher matcher = pattern.matcher(args);
            if(matcher.find()){
                switch (parameter){
                    case "id":
                        String id = matcher.group(3);
                        if(id != null) {
                            user.setId(Integer.parseInt(id));
                        } else {
                            // in save method it will trigger new User
                            user.setId(-1);
                        }
                        break;
                    case "firstname":
                        user.setFirstName(matcher.group(3));
                        break;
                    case "lastname":
                        user.setLastName(matcher.group(3));
                        break;
                    case "role":
                        user.setRole(matcher.group(3));
                        break;
                }
            }

        }
        // trigger new user in save method
        if(user.getId() == null) user.setId(-1);
        return user;
    }


    @Override
    public void execute(String args) {
        String command = getCommand(args, messenger);
        int id = getId(args, messenger);
        switch (command){
            case "-a": case"--all":
                messenger.print((List<User>)repository.findAll());
                break;
            case "-d":
                if(id != 0){
                    repository.deleteById(id);
                }
                break;
            case "--update":
                User user = setUserForUpdate(args);
                repository.save(user);

        }
        /* String z = args[0];
        switch (z) {
            case "-a":
            case "--all":
                messenger.print(getAll());
            case "-d":
            case "--delete":
                try {
                    int id = Integer.parseInt(util.getParameter("id", args));
                    messenger.print(deleteUser(id));
                } catch (Exception e) {
                    messenger.printError("Error while deleting user.");
                }
            case "-id":
                try {
                    int id = Integer.parseInt(util.getParameter("id", args));
                    messenger.print(getUser(id));
                } catch (Exception e) {
                    e.printStackTrace();
                    messenger.printError("Error while searching for user.");
                }
            case "--save":
                try {
                    messenger.print(saveUser(args));
                } catch (NoSuchElementException e) {
                    messenger.printError(wrongParameter);
                } catch (IllegalArgumentException    @Autowired
    Messenger messenger; e) {
                    messenger.printError(e.getMessage());
                }
            case "--update":
                try {
                    messenger.print(updateUser(args));
                } catch (NoSuchElementException e){
                    messenger.printError(wrongParameter);
                }

        }
    */
    }
}
