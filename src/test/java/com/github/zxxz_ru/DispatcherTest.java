package com.github.zxxz_ru;
import com.github.zxxz_ru.command.Dispatcher;
import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.BaseMatcher.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.zxxz_ru.BtsConfig;

import java.util.HashMap;
import java.util.Map;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BtsConfig.class)
public class DispatcherTest {

    @Autowired
    Dispatcher dispatcher;

    @Test
    public void testArgsMapping(){
        String[] args = {"--filepath", "/root/home/dir/file"};
        Map<String, String> map = dispatcher.getArgsMap(args);
        // assertTrue(map.containsKey("--filepath"));
        assertEquals("Error during parsing args Array. ",
                 "/root/home/dir/file", map.get("--filepath"));
    }

}
