package com.github.zxxz_ru.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="PROJECT")
public class Project {
    @Id
    @Column(name = "PROJECT_ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(name = "PROJECT_NAME")
    private String projectName;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(targetEntity = Task.class)
    private List taskList;

    public Project(){}

    public Project(Integer id, String projectName, String description) {
        this.id = id;
        this.projectName = projectName;
        this.description = description;
    }

    public  Project from(String projectName, String description){
        this.setProjectName(projectName);
        this.setDescription(description);
        return this;
    }

    @Override
    public String toString(){
        return new StringBuilder()
       .append("\n\t\t------Project---------")
       .append("\nProject ID: ").append(this.id)
       .append("\nProject Name: ").append(this.projectName)
       .append("\nDescription: ").append(this.description)
        .substring(0);
    }

    // setters & getters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List getTaskList() {
        return taskList;
    }

    public void setTaskList(List taskList) {
        this.taskList = taskList;
    }
}
