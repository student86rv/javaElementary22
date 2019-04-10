package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StorageDao implements Storage {

    private Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public StorageDao() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/newBase", "postgres", "student2019");
        createUsersTable();
    }

    private void createUsersTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS users (\n" +
                    "_id SERIAL PRIMARY KEY,\n" +
                    "name varchar(100),\n" +
                    "age int\n" +
                    ");");
        }
    }

    @Override
    public void removeAll() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int count = statement.executeUpdate("DELETE FROM users;");
            System.out.println("Deleted " + count + " rows from table users");
        }
    }

    @Override
    public void removeUser(int id) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("DELETE FROM users WHERE _id = ('%d');", id);
            statement.execute(request);
        }
    }

    @Override
    public void removeUserByName(String name) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("DELETE FROM users WHERE name = ('%s');", name);
            statement.execute(request);
        }
    }

    @Override
    public void addUser(User user) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("INSERT INTO users (name, age) VALUES ('%s', '%d');",
                                user.getName(), user.getAge());
            statement.execute(request);

            String idRequest = "SELECT COUNT(*) FROM users AS count;";
            ResultSet resultSet = statement.executeQuery(idRequest);
            int count = 0;
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            user.setId(count);
        }
    }

    @Override
    public void updateUser(User user) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("UPDATE users SET name = ('%s'), age = ('%d') WHERE _id = ('%d');",
                                user.getName(), user.getAge(), user.getId());
            statement.execute(request);
        }
    }

    @Override
    public User getUser(int id) throws SQLException {
        User user = new User();
        try (Statement statement = connection.createStatement()) {
            String request = String.format("SELECT * FROM users WHERE _id = ('%d');", id);
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                user.setId(resultSet.getInt("_id"));
                user.setName(resultSet.getString("name"));
                user.setAge(resultSet.getInt("age"));
            }
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users ORDER BY _id;");
            while (resultSet.next()) {
                int id = resultSet.getInt("_id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                users.add(new User(id, name, age));
            }
        }
        return users;
    }
}
