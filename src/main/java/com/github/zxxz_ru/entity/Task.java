package com.github.zxxz_ru.entity;

import javax.persistence.*;
import java.util.List;

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
    }

    public Task(Integer id, String thema, String priority, String taskType, String description) {
        this.id = id;
        this.thema = thema;
        this.priority = priority;
        this.taskType = taskType;
        this.description = description;
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
