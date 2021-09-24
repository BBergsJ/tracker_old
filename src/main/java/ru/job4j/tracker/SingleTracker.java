package ru.job4j.tracker;

public final class SingleTracker {
    private static Tracker tracker = null;

    /* реализовать singleton */

    private SingleTracker() {
    }

    public static Tracker getInstance() {
        if (tracker == null) {
            tracker = new Tracker();
        }
        return tracker;
    }
}
