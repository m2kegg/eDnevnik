package com.example.ednevnik;

import java.util.List;

public class Group {
    public String name;
    public User admin;
    public List<User> users;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Group(List<User> users, String name, User admin) {
        this.users = users;
        this.name = name;
        this.admin = admin;
    }

    public Group(String name, User admin)
    {
        this.name = name;
        this.admin = admin;
        this.users = null;
    }

    public Group() {
    }
}
