package com.github.zxxz_ru.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "PROJECT")
@JsonDeserialize(using = ProjectDeserializer.class)
public class Project implements StoreUnit {
    @Id
    @Column(name = "PROJECT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "PROJECT_NAME")
    private String projectName;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(targetEntity = Task.class)
    private List<Task> taskList;

    public Project() {
        this.taskList = new ArrayList<>();
    }

    public Project(Integer id, String projectName, String description) {
        this.id = id;
        this.projectName = projectName;
        this.description = description;
        this.taskList = new ArrayList<>();
    }


    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();
        bld.append("\nProject ID: ").append(this.id)
                .append("\nProject Name: ").append(this.projectName)
                .append("\nDescription: ").append(this.description);
        if (taskList.size() > 0) {
            bld.append("\nTasks: {");
            for (Task t : taskList) {
                bld.append(t.getId()).append(", ");
            }
            bld.replace(bld.length() - 2, bld.length(), "}\n");
        } else {
            bld.append("\n");
        }
        return bld.append("\n").substring(0);
    }

    public Optional<Task> getTaskById(int id) {
        Optional<Task> opti = Optional.empty();
        if (this.taskList.size() > 0) {
            List<Task> tasks = this.taskList.stream().filter(t -> t.getId() == id).collect(Collectors.toList());
            if (tasks.size() > 0) {
                opti = Optional.of(tasks.get(0));
            }
        }
        return opti;
    }

    @Override
    public <T extends StoreUnit> T from(T t) {
        Project p = (Project) t;
        if ((p.getId() != null)) {
            this.id = p.getId();
        }
        if (p.getProjectName() != null) {
            this.projectName = p.getProjectName();
        }
        if (p.getDescription() != null) {
            this.description = p.getDescription();
        }
        if (p.getTaskList().size() > 0) {
            // add only if not in list
            List<Task> tasks = p.getTaskList();
            for (Task ts : tasks) {
                Optional<Task> opti = getTaskById(ts.getId());
                if (opti.isEmpty()) {
                    this.taskList.add(ts);
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

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List taskList) {
        this.taskList = taskList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        Project t = (Project) o;
        return this.id.equals(t.getId()) && this.projectName.equals(t.getProjectName()) &&
                this.description.equals(t.getDescription());
    }

    @Override
    public int hashCode() {
        int id = this.id == null ? 1 : this.id;
        return 9 * (this.projectName.hashCode() +
                this.description.hashCode()) + id;
    }
}
