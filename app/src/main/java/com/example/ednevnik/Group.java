package com.example.ednevnik;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class Group {
    public String name;
    public DocumentReference admin;
    public List<DocumentReference> users;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Group(List<DocumentReference> users, String name, DocumentReference admin) {
        this.users = users;
        this.name = name;
        this.admin = admin;
    }

    public Group(String name, DocumentReference admin)
    {
        this.name = name;
        this.admin = admin;
        this.users = null;
    }

    public Group() {
    }
}
