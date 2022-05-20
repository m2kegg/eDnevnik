package com.example.ednevnik;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
