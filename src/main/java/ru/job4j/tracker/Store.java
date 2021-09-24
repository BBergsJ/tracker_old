package ru.job4j.tracker;

import java.util.List;

public interface Store extends AutoCloseable {
    void init();
    Item add(Item item) throws Exception;
    boolean replace(String id, Item item) throws Exception;
    boolean delete(String id) throws Exception;
    List<Item> findAll() throws Exception;
    List<Item> findByName(String key);
    Item findById(String id);
}