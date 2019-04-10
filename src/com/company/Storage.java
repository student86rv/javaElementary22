package com.company;

import java.sql.SQLException;
import java.util.List;

public interface Storage {

    void removeAll() throws SQLException;

    void removeUser(int id) throws SQLException;

    void removeUserByName(String name) throws SQLException;

    void addUser(User user) throws SQLException;

    void updateUser(User user) throws SQLException;

    User getUser(int id) throws SQLException;

    List<User> getAllUsers() throws SQLException;
}
