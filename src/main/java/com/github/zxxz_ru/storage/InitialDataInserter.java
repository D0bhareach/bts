package com.github.zxxz_ru.storage;

import com.github.zxxz_ru.AppState;
import com.github.zxxz_ru.storage.dao.ProjectRepository;
import com.github.zxxz_ru.storage.dao.TaskRepository;
import com.github.zxxz_ru.storage.dao.UserRepository;
import com.github.zxxz_ru.storage.file.Storage;
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
    // Fields required for File System Storage
    @Autowired
    AppState state;

    public List<User> createUserList(){
        List<User> list = new ArrayList<>(5);
        list.add(new User(1,"Александр","Иванов", "Developer"));
        list.add(new User(2, "Василий", "Петров", "Developer"));
        list.add(new User(3,"Роман","Володин", "Developer"));
        list.add(new User(4,"Владимир","Сердюк","Project Manager"));
        list.add(new User(5,"Олег","Торшин","CTO"));
        return list;
    }

    private void insertUsers(List<User> list){
        long counter = ure.count();
        if( counter == 0) {
            ure.saveAll(list);
        }

    }

    public List<Project> createProjectList(List tasks){
        List<Project> list = new ArrayList<>();
        Project project = new Project(1,"BTS", "Bug tracking System");
        project.setTaskList(List.of(tasks.get(0),tasks.get(1),tasks.get(2)));
        list.add(project);
        project = new Project(2,"Project_1", "Good Idea Project");
        project.setTaskList(List.of(tasks.get(3)));
        list.add(project);
        project = new Project(3,"Enigma", "SecretProject");
        project.setTaskList(List.of(tasks.get(2)));
        project = new Project(4,"Project_2", "Very important Project");
        list.add(project);
            return  list;
    }

    private void insertProjects(List<Project> projects){
        if(pre.count() == 0) {
            pre.saveAll(projects);
        }
    }

    public List<Task> createTaskList(List<User> users){
        List<Task> list = new ArrayList<>();
        Task task =new Task(1, "Bug 233 in BTS", "high",
                "",  "Nasty Bug in JPA somewhere." );
        task.setUserList(List.of(users.get(0), users.get(1),users.get(4)));
        list.add(task);

        task =new Task(2,"Bug 235 in BTS",
                "normal", "",
                "Null Pointer in Spring Boot.");
        task.setUserList(List.of(users.get(0), users.get(3)));
        list.add(task);

        task =new Task(3,"Bug 239 in BTS", "done", "",  "Simple typos in HelpCommand.");
        list.add(task);

        task =new Task(4, "Project_1 start", "normal", "",   "Good Idea Project Start and Git init.");
        task.setUserList(List.of(users.get(1), users.get(3)));
        list.add(task);

        task =new Task(5,"Enigma", "normal", "secret",   "VIP for FBI.") ;
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
