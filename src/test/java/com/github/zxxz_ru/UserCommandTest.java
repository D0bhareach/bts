package com.github.zxxz_ru;

import com.github.zxxz_ru.command.UserCommand;
import com.github.zxxz_ru.entity.StoreUnit;
import com.github.zxxz_ru.entity.Task;
import com.github.zxxz_ru.entity.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BtsConfig.class, FileSystemRepositoryConfig.class})
@Ignore
public class UserCommandTest {
    @Autowired
    private UserCommand command;
    @Autowired
    private CommandTestUtil util;
    private static boolean initialized = false;
    private static boolean cleared = false;

    @Before
    public  void initTests() {
        if(!initialized) {
            util.deleteDataFile();
            util.createDataFile();
            initialized = true;
        }
    }

    public  void clearAfter() {
            util.deleteDataFile();
            util.createDataFile();

    }

    @Test
    public void aa() {
        String args = "user -a";
        Optional<List<? extends StoreUnit>> o = command.execute(args);
        List<? extends StoreUnit> list = null;
        if (o.isPresent()) list = o.get();
        assertTrue("Expected 5 got " + list.size(), list.size() == 5);
    }

    @Test
    public void ab() {
        String args = "user --all";
        Optional<List<? extends StoreUnit>> o = command.execute(args);
        List<? extends StoreUnit> list = null;
        if (o.isPresent()) list = o.get();
        assertTrue("Expected 5 got " + list.size(), list.size() == 5);
    }


    @Test
    public void ac() {
        String[] args = {"user - all", "user -all", "user -- all"};
        for (String s : args) {
            Optional<List<? extends StoreUnit>> o = command.execute(s);
            assertEquals(Optional.empty(), o);
        }
    }

    @Test
    public void ad() {
        String[] args =
                {"user -id aaa", "user -id 4eee", "user -id www3", "user -id ", "user -id 0"};
        for (String s : args) {
            Optional<List<? extends StoreUnit>> o = command.execute(s);
            assertEquals("command " + s + "return non empty.", Optional.empty(), o);
        }
    }

    @Test
    public void ae() {
        String args = "user -id 1 ";
        Optional<List<? extends StoreUnit>> o = command.execute(args);
        User user = new User();
        if (o.isPresent()) {
            user = (User) o.get().get(0);
        }
        assertTrue(1 == user.getId());
    }

    @Test
    public void af() {
        String args = "user -id 1 --assign-task 3";
        Optional<List<? extends StoreUnit>> o = command.execute(args);
        Task task = new Task();
        User user = new User();
        List<User> users = null;
        if (o.isPresent()) {
            task = (Task) o.get().get(0);
            users = task.getUserList();
            assertTrue(users.size() == 1);
            user = users.get(0);
        }
        assertTrue(1 == user.getId());
    }

    @Test
    public void ag() {
        String args = "user -id 1 --drop-task 3";
        Optional<List<? extends StoreUnit>> o = command.execute(args);
        Task task = new Task();
        List<User> users = null;
        if (o.isPresent()) {
            task = (Task) o.get().get(0);
            users = task.getUserList();
            assertNotNull(users);

        }
        assertNotNull(users);
        assertTrue(users.size() == 0);
    }

    // Below are --update and -d / --delete tests
    // need to test counter resetting as well

    // create new
    @Test
    public void ba() {
        String args = "user --update firstname='Test 1' lastname='Last Name 1' role='Test 1'";
        Optional<List<? extends StoreUnit>> opti = command.execute(args);
        User user = new User();
        if (opti.isPresent()) {
            user = (User) opti.get().get(0);
        }
        assertEquals("Firstname of user is not Test 1.", "Test 1", user.getFirstName());
        assertTrue(6 == user.getId());
    }

    // user with id = 6 is created and ready
    @Test
    public void bb() {
        String args = "user --update id = 6  lastname='Updated 1'";
        Optional<List<? extends StoreUnit>> opti = command.execute(args);
        User user = new User();
        if (opti.isPresent()) {
            user = (User) opti.get().get(0);
        }
        assertEquals("Last name is not Updated 1!", "Updated 1", user.getLastName());

    }

    @Test
    public void bc() {
        String args = "user -d 6";
        Optional<List<? extends StoreUnit>> opti = command.execute(args);
        assertEquals(Optional.empty(), opti);
        opti = command.execute("user --all");
        List<? extends StoreUnit> list = null;
        if (opti.isPresent()) {
            list = opti.get();
        }
        assertNotNull(list);
        assertTrue(list.size() == 5);
        clearAfter();
    }
    // I will need after class metnod to reset counter for users


}
