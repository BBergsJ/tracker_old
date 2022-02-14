package ru.job4j.tracker;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlTracker implements Store, AutoCloseable {

    private Connection cn;

    public SqlTracker() {
        init();
    }

    public SqlTracker(Connection cn) {
        this.cn = cn;
    }

    public void init() {
        try (InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn =  DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (cn != null) {
            cn.close();
        }
    }

    @Override
    public Item add(Item item) {
        try (PreparedStatement ps = cn.prepareStatement("insert into items (name) values (?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getName());
            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                item.setId(id);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return item;
    }

    @Override
    public boolean replace(String id, Item item) {
        boolean rsl = false;
        try (PreparedStatement ps = cn.prepareStatement("update items set name = ? where id = ?")) {
            ps.setString(1, item.getName());
            ps.setInt(2, Integer.parseInt(id));
            rsl = ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return rsl;
    }

    @Override
    public boolean delete(String id) {
        boolean rsl = false;
        try (PreparedStatement ps = cn.prepareStatement("delete from items where id = ?")) {
            ps.setInt(1, Integer.parseInt(id));
            rsl = ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return rsl;
    }

    @Override
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement("select * from items")) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Item item = new Item(resultSet.getString("name"));
                item.setId(resultSet.getInt("id"));
                items.add(item);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return items;
    }

    public void findAllReact(Observe<Item> observe) {
        try (PreparedStatement ps = cn.prepareStatement("select * from items")) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                observe.receive(
                        new Item(
                                resultSet.getInt("id"),
                                resultSet.getString("name")
                        )
                );
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> items = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement("select * from items where name = ?")) {
            ps.setString(1, key);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Item item = new Item(resultSet.getString("name"));
                item.setId(resultSet.getInt("id"));
                items.add(item);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return items;
    }

    @Override
    public Item findById(String id) {
        Item item = new Item("empty object");
        try (PreparedStatement ps = cn.prepareStatement("select * from items where id = ?")) {
            ps.setInt(1, Integer.parseInt(id));
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                item.setName(resultSet.getString("name"));
                item.setId(resultSet.getInt("id"));
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return item;
    }

    @Override
    public void deleteAllItems() {
        try (PreparedStatement ps = cn.prepareStatement("truncate table items")) {
            ps.executeQuery();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}