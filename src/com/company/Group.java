package com.company;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private int id;
    private String name;
    private String startDate;
    private List<Student> students = new ArrayList<>();

    public Group() {
    }

    public Group(String name, String startDate) {
        this.name = name;
        this.startDate = startDate;
    }

    public Group(int id, String name, String startDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate='" + startDate + '\'' +
                '}';
    }
}
