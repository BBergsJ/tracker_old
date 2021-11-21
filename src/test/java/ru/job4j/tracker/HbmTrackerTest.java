package ru.job4j.tracker;

import org.hibernate.Session;
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

    private final Store tracker = HbmTracker.getInstance();

    @Before
    public void before() {
        tracker.deleteAllItems();
    }

    @Test
    public void whenAdd() throws Exception {
        Item item = new Item("test1");
        tracker.add(item);
        Item result = tracker.findById(String.valueOf(item.getId()));
        assertEquals(result.getName(), item.getName());
    }

    @Test
    public void whenReplace() throws Exception {
        Item item = new Item("test2");
        tracker.add(item);
        tracker.replace(String.valueOf(item.getId()), new Item("test3"));
        Item result = tracker.findById(String.valueOf(item.getId()));
        assertThat(result.getName(), is("test3"));
    }

    @Test
    public void whenDelete() throws Exception {
        Item item = new Item("test4");
        tracker.add(item);
        tracker.delete(String.valueOf(item.getId()));
        assertThat(tracker.findAll().size(), is(0));
    }

    @Test
    public void whenFindAll() throws Exception {
        Item itemOne = new Item("One");
        tracker.add(itemOne);
        Item itemTwo = new Item("Two");
        tracker.add(itemTwo);
        assertEquals(List.of(itemOne, itemTwo), tracker.findAll());
    }

    @Test
    public void whenFindByName() throws Exception {
        Item itemOne = new Item("One");
        tracker.add(itemOne);
        assertEquals(tracker.findByName("One"), List.of(itemOne));
    }

    @Test
    public void whenFindById() throws Exception {
        Item itemOne = new Item("One");
        tracker.add(itemOne);
        assertEquals(tracker.findById(String.valueOf(itemOne.getId())), itemOne);
    }
}