package com.github.zxxz_ru;

import com.github.zxxz_ru.storage.file.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Simple System.exit(0) is not efficient especially for mode when
 * File System Storage is used. Since I have no Idea how to work with Db Connection
 * in Spring. All this class will handle is case when File System is used.
 */
@Component
public class ApplicationCloser {
    @Autowired
    AppState state;
    @Autowired
    Storage storage;

    public void closeApp(int status) {
        if (state.getMode() == AppState.AppMode.FILESYSTEM)
            storage.writeData();
        System.exit(status);
    }
}
