package com.github.zxxz_ru;

import org.springframework.stereotype.Component;

/**
 * This Class will hold Application State.
 * During execution Components require Application State, for example
 * AppState.mode is required by classes implementing Commander to
 * load repository depending on state. For working with File System
 * AppState path field is required, so FileSystemRepository will open
 * File correctly.
 */
@Component
public class AppState {
    /*
    private final UserFileRepository userFileRepository;
    private final TaskFileRepository taskFileRepository;
    private final ProjectFileRepository projectFileRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
*/
    //   @Autowired
    /*
    public AppState(
            UserFileRepository uf,
            TaskFileRepository tf,
            ProjectFileRepository pf,
            UserRepository ur,
            TaskRepository tr,
            ProjectRepository pr

    ) {
        this.userRepository = ur;
        this.taskRepository = tr;
        this.projectRepository = pr;
        this.userFileRepository = uf;
        this.taskFileRepository = tf;
        this.projectFileRepository = pf;

    }

     */

    public enum AppMode {DATABASE, FILESYSTEM}

    // Application Storage Mode. App can use Database ar File System.
    // Default is File System
    private AppMode mode = AppMode.FILESYSTEM;

    public AppMode getMode() {
        return mode;
    }

    public void setMode(AppMode mode) {
        this.mode = mode;
    }

/*
    public <T extends CrudRepository<? extends StoreUnit, Integer>> T getUserRepository() {
        switch (mode) {
            case DATABASE:
                return (T) userRepository;
            case FILESYSTEM:
                return (T) userFileRepository;
        }
        return null;
    }
    public <T extends CrudRepository<? extends StoreUnit, Integer>> T getTaskRepository() {
        switch (mode) {
            case DATABASE:
                return (T) taskRepository;
            case FILESYSTEM:
                return (T) taskFileRepository;
        }
        return null;
    }
    public <T extends CrudRepository<? extends StoreUnit, Integer>> T getProjectRepository() {
        switch (mode) {
            case DATABASE:
                return (T) projectRepository;
            case FILESYSTEM:
                return (T) projectFileRepository;
        }
        return null;
    }

 */
}
