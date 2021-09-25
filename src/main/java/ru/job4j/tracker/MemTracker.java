package ru.job4j.tracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MemTracker {
    private final List<Item> items = new ArrayList<>();

    public Item add(Item item) {
        item.setId(generateId());
        items.add(item);
        return item;
    }

    public List<Item> findAll() {
        return items;
    }

    private int generateId() {
        Random rm = new Random();
        return (int) (rm.nextLong() + System.currentTimeMillis());
    }

    public List<Item> findByName(String key) {
        List<Item> itemsFinded = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(key)) {
                itemsFinded.add(items.get(i));
            }
        }
        return itemsFinded;
    }

    public Item findById(String id) {
        int index = indexOf(id);
        return index != -1 ? items.get(index) : null;
    }

    private int indexOf(String id) {
        int rsl = -1;
        for (int index = 0; index < items.size(); index++) {
            if (items.get(index).getId() == Integer.parseInt(id)) {
                rsl = index;
                break;
            }
        }
        return rsl;
    }

    public boolean replace(String id, Item item) {
        int index = indexOf(id);
        boolean rsl = index != -1;
        if (rsl) {
            items.set(index, item);
            item.setId(Integer.parseInt(id));
        }
        return rsl;
    }

    public boolean delete(String id) {
        int index = indexOf(id);
        boolean rsl = index != -1;
        if (rsl) {
            items.remove(index);
        }
        return rsl;
    }
}