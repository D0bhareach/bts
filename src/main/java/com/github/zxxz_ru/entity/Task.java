package com.github.zxxz_ru.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "TASK")
public class Task implements StoreUnit {
    @Id
    @Column(name = "TASK_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "THEMA")
    private String thema;

    @Column(name = "PRIORITY")
    private String priority;

    @Column(name = "TASK_TYPE")
    private String taskType;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(targetEntity = User.class)
    private List<User> userList;

    public Task() {
        this.userList = new ArrayList<User>();
    }

    public Task(Integer id, String thema, String priority, String taskType, String description) {
        this.id = id;
        this.thema = thema;
        this.priority = priority;
        this.taskType = taskType;
        this.description = description;
        this.userList = new ArrayList<User>();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("\nTask ID: ").append(this.id)
                .append("\nTask Thema: ").append(this.thema)
                .append("\nPriority: ").append(this.priority)
                .append("\nTask Type: ").append(this.taskType)
                .append("\nDescription: ").append(this.description)
                .append("\n").substring(0);
    }

    public Optional<User> getUserById(int id) {
        Optional opti = Optional.empty();
        if (this.userList.size() > 0) {
            List<User> users = userList.stream().filter(u -> u.getId() == id).collect(Collectors.toList());
            if (users.size() > 0) {
                opti = Optional.of(users.get(0));
            }
        }
        return opti;
    }

    @Override
    public <T extends StoreUnit> T from(T t) {
        Task n = new Task();
        Task task = (Task) t;
        if ((task.getId() != null)) {
            this.id = task.getId();
        }
        if (task.getThema() != null) {
            this.thema = task.getThema();
        }
        if (task.getPriority() != null) {
            this.priority = task.getPriority();
        }
        if (task.getTaskType() != null) {
            this.taskType = task.getTaskType();
        }
        if (task.getDescription() != null) {
            this.description = task.getDescription();
        }
        if (task.getUserList().size() > 0) {
            // add only if not in list
            List<User> users = task.getUserList();
            for (User u : users) {
                Optional opti = getUserById(u.getId());
                if (!opti.isPresent()) {
                    this.userList.add(u);
                }
            }

        }
        return (T) this;
    }
    // setters & getters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getThema() {
        return thema;
    }

    public void setThema(String thema) {
        this.thema = thema;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        Task t = (Task) o;
        return this.id.equals(t.getId()) && this.thema.equals(t.getThema()) &&
                this.taskType.equals(t.getTaskType()) && this.priority.equals(t.getPriority()) &&
                this.description.equals(t.getDescription());
    }

    @Override
    public int hashCode() {
        int id = this.id == null ? 1 : this.id;
        return 8 * (this.thema.hashCode() + this.taskType.hashCode() + this.priority.hashCode() +
                this.description.hashCode()) + id;
    }
}
