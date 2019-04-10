package com.company;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try {
            StorageDao storageDao = new StorageDao();
            storageDao.removeAll();

            storageDao.addUser(new User("Alex", 25));
            storageDao.addUser(new User("Oleg", 20));
            storageDao.addUser(new User("Dmitrii", 27));
            storageDao.addUser(new User("Anna", 19));
            storageDao.addUser(new User("Andrey", 31));
            System.out.println("Users: " + storageDao.getAllUsers());

            storageDao.updateUser(new User(1, "Victor", 24));
            System.out.println("Update user 1...");
            System.out.println("Users: " + storageDao.getAllUsers());

            System.out.println("Get user 2...");
            System.out.println("User 2: " + storageDao.getUser(2));

            storageDao.removeUser(3);
            System.out.println("Remove user 3...");
            System.out.println("Users: " + storageDao.getAllUsers());

            storageDao.removeUserByName("Anna");
            System.out.println("Remove user Anna...");
            System.out.println("Users: " + storageDao.getAllUsers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
