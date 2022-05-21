package com.example.ednevnik;

import com.google.firebase.firestore.DocumentReference;

public class Hometask {
    public String task;
    public DocumentReference lesson;

    public Hometask(String task, DocumentReference lesson) {
        this.task = task;
        this.lesson = lesson;
    }
}
