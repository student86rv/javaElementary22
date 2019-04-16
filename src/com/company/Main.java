package com.company;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        Group java = new Group("Java", "22.01.19");
        Student javaStudent1 = new Student("Alex", 25);
        java.getStudents().add(javaStudent1);

        Student javaStudent2 = new Student("Oleg", 20);
        java.getStudents().add(javaStudent2);

        Group cpp = new Group("CPP", "20.02.19");
        Student cppStudent1 = new Student("Dmitrii", 27);
        cpp.getStudents().add(cppStudent1);

        Student cppStudent2 = new Student("Anna", 19);
        cpp.getStudents().add(cppStudent2);

        Student max = new Student("Max", 29);
        java.getStudents().add(max);
        cpp.getStudents().add(max);

        try {
            StorageDao storageDao = new StorageDao();
            storageDao.removeAll();

            storageDao.addGroup(java);
            storageDao.addGroup(cpp);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
