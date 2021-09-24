package ru.job4j.tracker;

import java.sql.Connection;
import java.time.format.DateTimeFormatter;

public class StartUI {
    public void init(Input input, Store tracker, UserAction[] actions) throws Exception {
        boolean run = true;
        while (run) {
            this.showMenu(actions);
            int select = input.askInt("Select: ", actions.length);
            UserAction action = actions[select];
            run = action.execute(input, tracker);
        }
    }

//    public void init(Input input, MemTracker memTracker, List<UserAction> actions) {
//        boolean run = true;
//        while (run) {
//            this.showMenu(actions);
//            int select = input.askInt("Select: ", actions.size());
//            UserAction action = actions.get(select);
//            run = action.execute(input, memTracker);
//        }
//    }

    private void showMenu(UserAction[] actions) {
        System.out.println("Menu.");
        for (int index = 0; index < actions.length; index++) {
            System.out.println(index + ". " + actions[index].name());
        }
    }

    public static void main(String[] args) {
//        Input input = new ConsoleInput();
//        Input validate = new ValidateInput(input);
//        MemTracker memTracker = new MemTracker();
//        List<UserAction> actions = new ArrayList<>();
//        actions.add(new CreateAction());
//        actions.add(new ShowAllAction());
//        actions.add(new EditAction());
//        actions.add(new DeleteAction());
//        actions.add(new FindByIdAction());
//        actions.add(new FindByNameAction());
//        actions.add(new ExitAction());
//        new StartUI().init(validate, memTracker, actions);
//    }
        Input validate = new ValidateInput(
                new ConsoleInput()
        );

        try (Store tracker = new SqlTracker()) {
            UserAction[] actions = {
                    new CreateAction(),
                    new EditAction(),
                    new DeleteAction(),
                    new ShowAllAction(),
                    new FindByIdAction(),
                    new FindByNameAction(),
                    new ExitAction()
            };
            new StartUI().init(validate, tracker, actions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}