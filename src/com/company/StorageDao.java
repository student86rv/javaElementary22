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
                    "student_id int,\n" +
                    "group_id int,\n" +
                    "PRIMARY KEY (student_id, group_id)\n" +
                    ");");
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

    public void addGroup(Group group) throws SQLException {

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

        for (Student student : group.getStudents()) {

            if (student.getId() == 0) {
                addUser(student);
            }

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
            String request = String.format("SELECT * FROM students WHERE _id = ('%d');", id);
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                int studentId = resultSet.getInt("_id");
                student.setId(studentId);
                student.setName(resultSet.getString("name"));
                student.setAge(resultSet.getInt("age"));
                student.setGroups(getGroupsByStudentId(studentId));
            }
        }
        return student;
    }

    public Student getUserByName(String name) throws SQLException {
        Student student = new Student();
        try (Statement statement = connection.createStatement()) {
            String request = String.format("SELECT * FROM students WHERE name = ('%s');", name);
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                int studentId = resultSet.getInt("_id");
                student.setId(studentId);
                student.setName(resultSet.getString("name"));
                student.setAge(resultSet.getInt("age"));
                student.setGroups(getGroupsByStudentId(studentId));
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

    private List<Group> getGroupsByStudentId(int studentId) throws SQLException {
        List<Group> groupList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String request = String.format("SELECT _id AS \"group_id\"," +
                            " name AS \"group_name\", " +
                            "start_date FROM groups " +
                            "JOIN students_in_groups " +
                            "ON groups._id = students_in_groups.group_id" +
                            " WHERE students_in_groups.student_id = ('%d');",
                    studentId);
            ResultSet resultSet = statement.executeQuery(request);
            while (resultSet.next()) {
                int id = resultSet.getInt("group_id");
                String name = resultSet.getString("group_name");
                String startDate = resultSet.getString("start_date");
                groupList.add(new Group(id, name, startDate));
            }
        }
        return groupList;
    }
}
