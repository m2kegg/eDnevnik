package com.example.ednevnik.POJO;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class User {
    public String email, login, pass, uid;
    public boolean isTeacher;

    public User() {
    }

    public User(String email, String login, String pass, boolean isTeacher, String uid) {
        this.email = email;
        this.login = login;
        this.pass = pass;
        this.isTeacher = isTeacher;
        this.uid = uid;
    }
    public static String listToString(ArrayList<User> users){
        StringBuilder str = new StringBuilder();
        for (User user:
             users) {
            str.append(user.login);
            str.append("\n");
        }
        return str.toString();
    }

}
