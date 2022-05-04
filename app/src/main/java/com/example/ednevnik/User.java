package com.example.ednevnik;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class User {
    public String email, login, pass;
    public boolean isTeacher;
    public List<Group> groups;

    public User() {
    }

    public User(String email, String login, String pass, boolean isTeacher) {
        this.email = email;
        this.login = login;
        this.pass = pass;
        this.isTeacher = isTeacher;
        this.groups = null;
    }
    public static User getUserFromUid(String uid){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final User[] user = {new User()};
        FirebaseDatabase.getInstance().getReference("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user[0] = snapshot.getValue(User.class);
        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return user[0];
    }
}
