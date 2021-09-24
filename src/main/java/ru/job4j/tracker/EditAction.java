package ru.job4j.tracker;

public class EditAction implements UserAction {
    @Override
    public String name() {
        return "===     Edit item     ====";
    }

    @Override
    public boolean execute(Input input, Store memTracker) throws Exception {
        String oldId = input.askStr("Enter old Id: ");
        Item newName = new Item(input.askStr("Enter new name: "));
        if (memTracker.replace(oldId, newName)) {
            System.out.println("Success!");
        } else {
            System.out.println("Error!");
        }
        return true;
    }
}
