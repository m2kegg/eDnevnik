package com.example.myapplication;

import com.orm.SugarRecord;

public class UsersDatabase extends SugarRecord {
    String email, login, pass;

    public UsersDatabase(String email, String login, String pass) {
        this.email = email;
        this.login = login;
        this.pass = pass;
    }

    public UsersDatabase() {
    }
}
