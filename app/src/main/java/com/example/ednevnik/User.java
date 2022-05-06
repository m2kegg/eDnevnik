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
    public String email, login, pass;
    public boolean isTeacher;

    public User() {
    }

    public User(String email, String login, String pass, boolean isTeacher) {
        this.email = email;
        this.login = login;
        this.pass = pass;
        this.isTeacher = isTeacher;
    }

}
