package ru.job4j.tracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HbmTracker implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    @Override
    public boolean replace(String id, Item item) {
        boolean rsl = false;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Item newItem = session.get(Item.class, Integer.parseInt(id));
            newItem.setName(item.getName());
            newItem.setCreated(item.getCreated());
            newItem.setCreatedTimestamp(item.getCreatedTimestamp());
            session.update(newItem);
            session.getTransaction().commit();
            rsl = true;
        }
        return rsl;

    }

    @Override
    public boolean delete(String id) {
        boolean rsl = false;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Item item = session.load(Item.class, Integer.parseInt(id));
            session.delete(item);
            session.getTransaction().commit();
            rsl = true;
        }
        return rsl;
    }

    @Override
    public List<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List items = session.createQuery("from Item").list();
        session.getTransaction().commit();
        session.close();
        return items;
    }

    @Override
    public List<Item> findByName(String key) {
        Session session = sf.openSession();
        session.beginTransaction();
        List items = session.createQuery("from ru.job4j.tracker.Item where name = :key")
                .setParameter("key", key)
                .list();
        session.getTransaction().commit();
        session.close();
        return items;
    }

    @Override
    public Item findById(String id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item item = session.get(Item.class, Integer.parseInt(id));
        session.getTransaction().commit();
        session.close();
        return item;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}