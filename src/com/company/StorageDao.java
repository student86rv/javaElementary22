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
        createStudentsTable();
        createGroupsTable();
        createStudentsInGroupsTable();
    }

    private void createStudentsTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS students (\n" +
                    "_id SERIAL PRIMARY KEY,\n" +
                    "name varchar(100),\n" +
                    "age int\n" +
                    ");");
        }
    }

    private void createGroupsTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS groups (\n" +
                    "_id SERIAL PRIMARY KEY,\n" +
                    "name varchar(100),\n" +
                    "start_date varchar(100)\n" +
                    ");");
        }
    }

    private void createStudentsInGroupsTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS students_in_groups (\n" +
                    "student_id int PRIMARY KEY,\n" +
                    "group_id int\n" +

                    ");");
            //"CONSTRAINT pk PRIMARY KEY (student_id, LastName)" +
        }
    }

    @Override
    public void removeAll() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int sCount = statement.executeUpdate("DELETE FROM students;");
            System.out.println("Deleted " + sCount + " rows from table students");

            int gCount = statement.executeUpdate("DELETE FROM groups;");
            System.out.println("Deleted " + gCount + " rows from table groups");

            int sgCount = statement.executeUpdate("DELETE FROM students_in_groups;");
            System.out.println("Deleted " + sgCount + " rows from table students_in_groups");
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
    public void addUser(Student student) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("INSERT INTO students (name, age) VALUES ('%s', '%d');",
                                student.getName(), student.getAge());
            statement.execute(request);

            String idRequest = "SELECT COUNT(*) FROM students AS count;";
            ResultSet resultSet = statement.executeQuery(idRequest);
            int count = 0;
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            student.setId(count);
        }
    }

    public void addGroup(Group group) throws SQLException{

        try (Statement statement = connection.createStatement()) {
            String request = String.format("INSERT INTO groups (name, start_date) VALUES ('%s', '%s');",
                    group.getName(), group.getStartDate());
            statement.execute(request);

            String idRequest = "SELECT COUNT(*) FROM groups AS count;";
            ResultSet resultSet = statement.executeQuery(idRequest);
            int count = 0;
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            group.setId(count);
        }

        for (Student student: group.getStudents()) {
            addUser(student);

            try (Statement statement = connection.createStatement()) {
                String request = String.format("INSERT INTO students_in_groups (student_id, group_id) VALUES ('%d', '%d');",
                        student.getId(), group.getId());
                statement.execute(request);
            }
        }
    }

    @Override
    public void updateUser(Student student) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String request = String.format("UPDATE users SET name = ('%s'), age = ('%d') WHERE _id = ('%d');",
                                student.getName(), student.getAge(), student.getId());
            statement.execute(request);
        }
    }

    @Override
    public Student getUser(int id) throws SQLException {
        Student student = new Student();
        try (Statement statement = connection.createStatement()) {
            String request = String.format("SELECT * FROM users WHERE _id = ('%d');", id);
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                student.setId(resultSet.getInt("_id"));
                student.setName(resultSet.getString("name"));
                student.setAge(resultSet.getInt("age"));
            }
        }
        return student;
    }

    @Override
    public List<Student> getAllUsers() throws SQLException {
        List<Student> students = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students ORDER BY _id;");
            while (resultSet.next()) {
                int id = resultSet.getInt("_id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                students.add(new Student(id, name, age));
            }
        }
        return students;
    }
}
