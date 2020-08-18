package com.github.zxxz_ru;

import com.github.zxxz_ru.command.Messenger;
import com.github.zxxz_ru.storage.InitialDataInserter;
import com.github.zxxz_ru.storage.file.Storage;
import com.github.zxxz_ru.storage.file.StorageFileCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class CommandTestUtil {
    @Autowired
    StorageFileCreator fileCreator;
    @Autowired
    Messenger messenger;

    public CommandTestUtil() {
    }

    void deleteDataFile(){
        Path path = fileCreator.createAbsolutePath();
        if(Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void createDataFile(){
        Storage storage = new Storage(messenger, fileCreator);
    }
}
