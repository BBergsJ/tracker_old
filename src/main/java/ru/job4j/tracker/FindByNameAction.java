package ru.job4j.tracker;

import java.util.List;

public class FindByNameAction implements UserAction {
    @Override
    public String name() {
        return "===  Find item by name ====";
    }

    @Override
    public boolean execute(Input input, Store store) {
        String findName = input.askStr("Enter Name: ");
        List<Item> names = store.findByName(findName);
        if (names.size() > 0) {
            for (Item item : names) {
                System.out.println(item);
            }
        } else {
            System.out.println("Empty array");
        }
        return true;
    }
}
