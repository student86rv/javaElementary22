package com.company;

import java.sql.SQLException;
import java.util.List;

public interface Storage {

    void removeAll() throws SQLException;

    void removeUser(int id) throws SQLException;

    void removeUserByName(String name) throws SQLException;

    void addUser(Student student) throws SQLException;

    void updateUser(Student student) throws SQLException;

    Student getUser(int id) throws SQLException;

    List<Student> getAllUsers() throws SQLException;
}
