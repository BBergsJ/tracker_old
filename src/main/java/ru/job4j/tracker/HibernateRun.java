package ru.job4j.tracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


public class HibernateRun {
    public static void main(String[] args) {
//        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
//                .configure().build();
//        try {
//            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
//            Item item = create(new Item("Learn Hibernate"), sf);
//            System.out.println(item);
//            item.setName("Learn Hibernate 5.");
//            update(item, sf);
//            System.out.println(item);
//            Item rsl = findById(item.getId(), sf);
//            System.out.println(rsl);
//            delete(rsl.getId(), sf);

//            Item itemOne = create(new Item("old 1", new Timestamp(1)), sf);
//            Item itemTwo = create(new Item("old 2", new Timestamp(2)), sf);
//            Item itemThree = create(new Item("old 3", new Timestamp(3)), sf);
//            List<Item> list = findAll(sf);
//            for (Item it : list) {
//                System.out.println(it);
//            }
//        }  catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            StandardServiceRegistryBuilder.destroy(registry);
//        }
        HbmTracker hbmTracker = new HbmTracker();
        hbmTracker.init();

        Item test = hbmTracker.findById("16");

        System.out.println(System.lineSeparator());
        hbmTracker.replace("30", test);
        for (Item item : hbmTracker.findAll()) {
            System.out.println(item.getName() + " " + item.getId());
        }
    }

    public static Item create(Item item, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    public static void update(Item item, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
    }

    public static void delete(Integer id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item item = new Item(null);
        item.setId(id);
        session.delete(item);
        session.getTransaction().commit();
        session.close();
    }

    public static List<Item> findAll(SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from ru.job4j.tracker.Item").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public static Item findById(Integer id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item result = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }
}