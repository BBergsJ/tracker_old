package ru.job4j.tracker;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.tracker.model.City;
import ru.job4j.tracker.store.CityStore;

import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class HbmTrackerTest {

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