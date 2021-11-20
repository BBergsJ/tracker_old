package ru.job4j.tracker;

import org.junit.*;
import ru.job4j.tracker.model.City;
import ru.job4j.tracker.store.CityStore;

import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class HbmTrackerTest {

    static Connection connection;

    @BeforeClass
    public static void init() {
        try (InputStream in = HbmTracker.class.getClassLoader().getResourceAsStream("test.properties")) {
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
    public void whenAdd() {
        HbmTracker hbmTracker = new HbmTracker();
        Item item = new Item("test1");
        hbmTracker.add(item);
        List<Item> all = hbmTracker.findAll();
        assertEquals(item, all.get(0));
    }

    @Test
    public void whenReplace() {
        HbmTracker hbmTracker = new HbmTracker();
        Item item = new Item("test2");
        hbmTracker.add(item);
        item.setName("changed");
        hbmTracker.replace("1", item);
        assertThat(hbmTracker.findAll().get(0).getName(), is("changed"));
    }

    @Test
    public void whenDelete() {
        HbmTracker hbmTracker = new HbmTracker();
        Item itemOne = new Item("One");
        hbmTracker.add(itemOne);
        hbmTracker.delete("1");
        assertThat(hbmTracker.findAll().size(), is(0));
    }

    @Test
    public void whenFindAll() {
        HbmTracker hbmTracker = new HbmTracker();
        Item itemOne = new Item("One");
        hbmTracker.add(itemOne);
        Item itemTwo = new Item("Two");
        hbmTracker.add(itemTwo);
        assertEquals(List.of(itemOne, itemTwo), hbmTracker.findAll());
    }

    @Test
    public void whenFindByName() {
        HbmTracker hbmTracker = new HbmTracker();
        Item itemOne = new Item("One");
        hbmTracker.add(itemOne);
        assertEquals(hbmTracker.findByName("One"), List.of(itemOne));
    }

    @Test
    public void whenFindById() {
        HbmTracker hbmTracker = new HbmTracker();
        Item itemOne = new Item("One");
        hbmTracker.add(itemOne);
        assertEquals(hbmTracker.findById("1"), itemOne);
    }
}