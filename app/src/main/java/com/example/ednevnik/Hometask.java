package com.example.ednevnik;

import com.google.firebase.firestore.DocumentReference;

public class Hometask {
    public String task;
    public DocumentReference group;
    public Hometask(){

    }
    public Hometask(String task, DocumentReference group) {
        this.task = task;
        this.group = group;
    }
}
