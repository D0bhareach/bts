package com.github.zxxz_ru.storage.file;

import com.github.zxxz_ru.command.Messenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@PropertySource("classpath:/application.properties")
public class StorageFileCreator {
    @Autowired
    Messenger messenger;

    @Value("${default.file.path}")
    private String defaultPath;

    private String path;

    /**
     * Parameter must be valid. It may not contain double slashes and dots.
     * This path is relative to user/home.
     *
     * @param path valid path string. It may be absolute (give full path to file),
     *             start from ~/ to which would be replaced with user home directory,
     *             start with slash or do not start with slash, path still be joined with user's
     *             home directory. Parameter must end with File Name. (no slash at the end)
     */
    public StorageFileCreator setPath(String path) {
        this.path = path.trim();
        return this;
    }

    private String getPath() {
        if (path != null) return path;
        return defaultPath;
    }

    /**
     * This method is used to create absolute path to Storage File.
     * It will try to construct Path from field path if it's not set equals null
     * Path will be constructed from default Path taken from application.properties.
     * ~ at path start will be replaced with user.home directory.
     * if there is slash at start of path this path is appended to user.home directory
     * as is. If no slash it still ends in user directory.
     * if field path == null default path is going to be used again in user.home.
     *
     * @return Path for file.
     */
    public Path createAbsolutePath() {
        String home = System.getProperty("user.home");
        final String slash = System.getProperty("file.separator");
        String path = getPath();
        Matcher m1 = Pattern.compile("^~" + slash).matcher(path);
        Matcher m2 = Pattern.compile(home + "(\\/\\w+)*").matcher(path);
        Matcher m3 = Pattern.compile("^(\\w+\\/?)(\\w+\\/?)?").matcher(path);
        if (m1.find()) {
            return Paths.get(m1.replaceFirst(home + slash));
        } else if (path.startsWith(slash)) {
            if (m2.find()) {
                return Paths.get(path);
            } else {
                return Paths.get(home + path);
            }
        } else if (m3.find()) {
            return Paths.get(home + slash + path);
        }
        return Paths.get(home + slash + defaultPath);
    }

    private void createDir(Path path) {
        Path dir = path.getParent();
        if (Files.exists(dir)) return;
        try {
            Files.createDirectories(dir, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-----")));
        } catch (IOException e) {
            messenger.print("Error during creating directory: " + dir.toString());
            // e.printStackTrace();
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
