package com.github.zxxz_ru;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:/application.properties")
public class AppState {
    public enum AppMode {DATABASE, FILESYSTEM}

    // Application Storage Mode. App can use Database ar File System.
    // Default is File System
    private AppMode mode = AppMode.FILESYSTEM;
    @Value("${default.file.path}")
    private String path;

    public AppMode getMode() {
        return mode;
    }

    public void setMode(AppMode mode) {
        this.mode = mode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
