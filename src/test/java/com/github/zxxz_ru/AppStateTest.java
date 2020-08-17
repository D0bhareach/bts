package com.github.zxxz_ru;
import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.BaseMatcher.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.github.zxxz_ru.BtsConfig;
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BtsConfig.class/*, loader = AnnotationConfigContextLoader.class*/)
public class AppStateTest {
    @Autowired
    AppState state;
    private static final Logger logger = LoggerFactory.getLogger(AppStateTest.class);


    @Test
    public void testDefaultMode(){
        assertEquals("Default mode not FILESYSTEM.",state.getMode(), AppState.AppMode.FILESYSTEM);
    }
}
