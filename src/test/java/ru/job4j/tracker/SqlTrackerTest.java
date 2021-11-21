package ru.job4j.tracker;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class SqlTrackerTest {

    static Connection connection;

    @BeforeClass
    public static void init() {
        try (InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @After
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("delete from items")) {
            statement.execute();
        }
    }

    @Test
    public void whenSaveItemAndFindByGeneratedIdThenMustBeTheSame() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        tracker.add(item);
        assertThat(tracker.findById(String.valueOf(item.getId())), is(item));
    }

    @Test
    public void createItem() throws Exception {
        SqlTracker tracker = new SqlTracker(connection);
        tracker.add(new Item("name"));
        assertThat(tracker.findByName("name").size(), is(1));
    }

    @Test
    public void whenFindByNameThenFinded() throws Exception {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("test1");
        tracker.add(item);
        List<Item> result = tracker.findByName("test1");
        assertThat(result.get(0).getName(), is(item.getName()));
    }

    @Test
    public void whenFullArrayThenFindAll() throws Exception {
        SqlTracker tracker = new SqlTracker(connection);
        Item item1 = new Item("test1");
        tracker.add(item1);
        List<Item> items = List.of(item1);
        List<Item> result = tracker.findAll();
        assertThat(result.get(result.size() - 1).getName(), is(item1.getName()));
    }

    @Test
    public void whenReplace() throws Exception {
        SqlTracker tracker = new SqlTracker(connection);
        Item bug = new Item("Bug");
        tracker.add(bug);
        String id = String.valueOf(bug.getId());
        Item replacer = new Item("Replacer");
        tracker.replace(id, replacer);
        assertThat(tracker.findById(id).getName(), is("Replacer"));
    }

    @Test
    public void whenDelete() throws Exception {
        SqlTracker tracker = new SqlTracker(connection);
        Item bug = new Item("Bug");
        tracker.add(bug);
        String id = String.valueOf(bug.getId());
        tracker.delete(id);
        assertThat(tracker.findById(id).getName(), is("empty object"));
    }
}