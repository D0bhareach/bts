package com.github.zxxz_ru.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TASK")
public class Task {
    @Id
    @Column(name = "TASK_ID")
    @GeneratedValue
    private Integer id;

    @Column(name = "THEMA")
    private String thema;

    @Column(name = "PRIORITY")
    private String priority;

    @Column(name = "TASK_TYPE")
    private String taskType;

    @Column(name = "PROJECT_ID")
    private Integer projectId;

    @Column(name = "DESCRIPTION")
    private String description;

    protected Task(){}

    public Task(Integer id, String thema, String priority, String taskType, Integer projectId, String description) {
        this.id = id;
        this.thema = thema;
        this.priority = priority;
        this.taskType = taskType;
        this.projectId = projectId;
        this.description = description;
    }

    @Override
    public String toString(){
        return new StringBuilder()
                .append("\nTask ID: ").append(this.id)
                .append("\nTask Thema: ").append(this.thema)
                .append("\nPriority: ").append(this.priority)
                .append("\nTask Type: ").append(this.taskType)
                .append("\nProjectId: ").append(this.projectId)
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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
