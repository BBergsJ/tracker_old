package ru.job4j.tracker;

import java.util.List;

public class ShowAllAction implements UserAction {
    @Override
    public String name() {
        return "===   Show all items  ====";
    }

    @Override
    public boolean execute(Input input, Store store) throws Exception {
        List<Item> allElem = store.findAll();
        for (Item item:allElem) {
            System.out.println(item);
        }
        return true;
    }
}
