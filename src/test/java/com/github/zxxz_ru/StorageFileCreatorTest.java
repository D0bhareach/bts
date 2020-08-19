package com.github.zxxz_ru;

import com.github.zxxz_ru.storage.file.StorageFileCreator;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BtsConfig.class, FileSystemRepositoryConfig.class, RepositoryCreatorConfig.class})
@Ignore
public class StorageFileCreatorTest {
    @Autowired
    StorageFileCreator creator;

    @Test
    public void absolutPath() {
        String[] args = {
                "~/test/dir/file",
                "test/dir/file",
                "/home/zxxz/test/dir/file",
                "/test/dir/file"
        };
        Path test = Paths.get("/home/zxxz/test/dir/file");
        for (int i = 0; i < args.length; i++) {
            creator.setPath(args[i]);
            Path path = creator.createAbsolutePath();
            assertEquals("Test# " + (i + 1), test, path);
        }
    }

    @Test
    public void absolutDefaultPath() {
        String[] args = {
                "~/test/dir/file",
                "test/dir/file",
                "/home/zxxz/test/dir/file",
                "/test/dir/file"
        };
        Path test = Paths.get("/home/zxxz/test/dir/file");
        Path path = creator.createAbsolutePath();
        assertEquals("Test Default Path ", test, path);
    }
}
