package com.github.zxxz_ru.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.zxxz_ru.entity.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitialDataInserter {
    @Autowired
    UserRepository ure;
    @Autowired
    ProjectRepository pre;
    @Autowired
    TaskRepository tre;

    private List<User> createUserList(){
        List<User> list = new ArrayList<>(5);
        User u = new User();
        u.setFirstName("Александр");
        u.setLastName("Иванов");
        u.setRole("Developer");
        list.add(u);

        u = new User();
        u.setFirstName("Василий");
        u.setLastName("Петров");
        u.setRole("Developer");
        list.add(u);

        u = new User();
        u.setFirstName("Роман");
        u.setLastName("Володин");
        u.setRole("Developer");
        list.add(u);

        u = new User();
        u.setFirstName("Владимир");
        u.setLastName("Сердюк");
        u.setRole("Project Manager");
        list.add(u);

        u = new User();
        u.setFirstName("Олег");
        u.setLastName("Торшин");
        u.setRole("CTO");
        list.add(u);
        return list;
    }

    private void insertUsers(List<User> list){
        long counter = ure.count();
        if( counter == 0) {
            ure.saveAll(list);
        }

    }

    private List<Project> createProjectList(List tasks){
        List<Project> list = new ArrayList<>();
        Project project;
        project = new Project();
        project = project.from("BTS", "Bug tracking System");
        project.setTaskList(List.of(tasks.get(0),tasks.get(1),tasks.get(2)));
        list.add(project);

        project = new Project();
        project = project.from("Project_1", "Good Idea Project");
        project.setTaskList(List.of(tasks.get(3)));
        list.add(project);

        project = new Project();
        project = project.from("Enigma", "SecretProject");
        project.setTaskList(List.of(tasks.get(2)));
        list.add(project);

        project = new Project();
        project = project.from("Project_2", "Very important Project");
        list.add(project);
            return  list;
    }

    private void insertProjects(List<Project> projects){
        if(pre.count() == 0) {
            pre.saveAll(projects);
        }
    }

    private List<Task> createTaskList(List<User> users){
        List<Task> list = new ArrayList<>();
        Task task =new Task();
                task = task.from("Bug 233 in BTS", "high",
                "",  "Nasty Bug in JPA somewhere." );
        task.setUserList(List.of(users.get(0), users.get(1),users.get(4)));
        list.add(task);

        task =new Task();
                task = task.from("Bug 235 in BTS",
                "normal", "",
                "Null Pointer in Spring Boot.");
        task.setUserList(List.of(users.get(0), users.get(3)));
        list.add(task);

        task =new Task();
        task = task.from("Bug 239 in BTS", "done", "",  "Simple typos in HelpCommand.");
        list.add(task);

        task =new Task();
        task = task.from("Project_1 start", "normal", "",   "Good Idea Project Start and Git init.");
        task.setUserList(List.of(users.get(1), users.get(3)));
        list.add(task);

        task =new Task();
        task = task.from("Enigma", "normal", "secret",   "VIP for FBI.") ;
        task.setUserList(List.of(users.get(2), users.get(4)));
        list.add(task);
        return list;
    }

    private void insertTasks(List<Task> tasks){
        if(tre.count() == 0) {
            tre.saveAll(tasks);
        }
    }

    public void insert(){
        List users = createUserList();
        List tasks = createTaskList(users);
        List projects = createProjectList(tasks);
        // these three lines places are important
        // users-tasks-projects, DO NOT CHANGE.
        insertUsers(users);
        insertTasks(tasks);
        insertProjects(projects);
    }

}
