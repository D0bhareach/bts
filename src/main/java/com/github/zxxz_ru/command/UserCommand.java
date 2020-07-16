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
    @Autowired
    private Util<User> util;

    private List<User> getAll() {
        return (List<User>) re.findAll();
    }


    private List<User> getUser(int id) throws NoSuchElementException {
        List<User> res = new ArrayList<>();
        Optional<User> opt = re.findById(id);
        opt.ifPresent(user -> res.add((User) user));
        return res;
    }

    private List<User> deleteUser(int id) {
        List<User> list = getUser(id);
        if (list.size() != 0) {
            re.delete(list.get(0));
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
    private List<User> updateUser(String... args) throws NoSuchElementException{
        List<User> list = new ArrayList<>();
        User user;
        // no more fun!
        if (!isPresent("id", args)) util.printError("user --put id is required parameter.");
        int userId;
        // get userId from parameters or create new.
            userId = Integer.parseInt(util.getParameter("id", args));
            Optional<User> userOption = re.findById(userId);
            if (userOption.isEmpty()) {
                util.printMessage("No user with id: " + userId);
            }
            user = userOption.get();
            if (isPresent("firstname", args)) {
                String firstName = util.getParameter("firstname",args);
                user.setFirstName(firstName);
            }
            if (isPresent("lastname", args)) {
                String lastName = util.getParameter("lastname",args);
                user.setLastName(lastName);
            }
            // Role Parameter not null and will be either default value or value provided in parameters.
            String role = getRoleParameter(args);
            user.setRole(role);
            user = re.save(user);
            list.add(user);
        return list;
    }
// TODO: When inserting witout id id is null!!!!
    private List<User> saveUser(String... args) throws NoSuchElementException, IllegalArgumentException {
        List<User> list = new ArrayList<>();
        User user = createNewUser(args);
        list.add(user);
        return list;
    }


    @Override
    public void execute(String... args) {
        String z = args[0];
        String wrongParameter = "Wrong usage! See help. [-h; --help]";
        switch (z) {
            case "-a":
            case "--all":
                util.print(getAll());
            case "-d":
            case "--delete":
                try {
                    int id = Integer.parseInt(util.getParameter("id", args));
                    util.print(deleteUser(id));
                } catch (Exception e) {
                    util.printError("Error while deleting user.");
                }
            case "-id":
                try {
                    int id = Integer.parseInt(util.getParameter("id", args));
                    util.print(getUser(id));
                } catch (Exception e) {
                    e.printStackTrace();
                    util.printError("Error while searching for user.");
                }
            case "--save":
                try {
                    util.print(saveUser(args));
                } catch (NoSuchElementException e) {
                    util.printError(wrongParameter);
                } catch (IllegalArgumentException e) {
                    util.printError(e.getMessage());
                }
            case "--update":
                try {
                    util.print(updateUser(args));
                } catch (NoSuchElementException e){
                    util.printError(wrongParameter);
                }

        }
        System.exit(0);
    }
}
