package com.github.zxxz_ru.command;

import com.github.zxxz_ru.storage.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import com.github.zxxz_ru.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class UserCommand implements Commander {

    @Autowired
    private UserRepository repository;
    // TODO: Must set repository according to AppState. Database of File.
    @Autowired
    private Util<User> util;
    @Autowired
    private Messenger messenger;

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

    private String getRoleParameter(String... args) throws NoSuchElementException {
        if (Arrays.stream(args).anyMatch(s -> Pattern.matches("role(=){1}.*", s))) {
            return util.getParameter("role", args);
        }
        return "Developer";
    }

    private boolean isPresent(String param, String... args) {
        String rex = param + "(=){1}.*";
        return Arrays.stream(args).anyMatch((a) -> Pattern.matches(rex, a));
    }

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

    // Update method only one required parameter id.
    // Must get user from Db if not print message that user do not exists.
    // return updated user for reference.
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

    // TODO: When inserting without id id is null!!!!
    private List<User> saveUser(String... args) throws NoSuchElementException, IllegalArgumentException {
        List<User> list = new ArrayList<>();
        User user = createNewUser(args);
        list.add(user);
        return list;
    }


    @Override
    public void execute(String args) {
        String wrongParameter = "Wrong usage! See help. [-h; help]";
        Pattern pattern = Pattern.compile("(-{1,2}\\w+)(\\s(\\d+))?");
        Matcher matcher = pattern.matcher(args);
        String command="";
        int value=0;
        if (matcher.find()) {
            for (int i = 1; i <= 2; i++) {
                if (i == 1) {
                    command = matcher.group(1);
                } else if(i == 2){
                    try {
                        String tmp = matcher.group(2);
                        tmp = tmp != null? tmp.trim(): "No integer!"; // do not pass null to parser
                        value = Integer.parseInt(tmp);
                    }catch (NumberFormatException e){
                        // do not want to fall will ask user to check input.
                    }
                    if(value == 0){
                        messenger.printError(wrongParameter);
                        messenger.printError("Check parameter's value");
                        return;
                    }
                }
            }
        }
        System.out.println("Command: "+command+". Value: "+value);
        // Check if command is id send input to handleIdCommand(String str)
        // If command is update send input to handleUpdate(String str)

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
                } catch (IllegalArgumentException e) {
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
