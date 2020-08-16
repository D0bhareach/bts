package com.github.zxxz_ru.storage.file;

import com.github.zxxz_ru.AppState;
import com.github.zxxz_ru.command.Messenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;

@Component
public class StorageFileCreator {
    @Autowired
    AppState state;
    @Autowired
    Messenger messenger;

    public Path createAbsolutePath() {
        return Paths.get(System.getProperty("user.home"), state.getPath());
    }

    private void createDir(Path path) {
        Path dir = path.getParent();
        if (Files.exists(dir)) return;
        try {
            Files.createDirectories(dir, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-----")));
        } catch (IOException e) {
            messenger.print("Error during creating directory: " + dir.toString());
            e.printStackTrace();
        }
    }

    public File createStorageFile() {
        Path path = createAbsolutePath();
        File file = null;
        if (!Files.exists(path)) {
            Path filePath;
            createDir(path);
            try {
                filePath = Files.createFile(path, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-----")));
                file = new File(filePath.toUri());
            } catch (IOException e) {
                messenger.print("Error while creating file: " + path);
            }
        } else if (Files.exists(path)) {
            file = new File(path.toUri());
        }
        return file;
    }
}
