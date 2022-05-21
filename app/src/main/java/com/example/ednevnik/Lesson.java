package com.example.ednevnik;

import android.location.Address;
import android.text.format.Time;

import com.google.firebase.firestore.DocumentReference;

import org.threeten.bp.LocalDate;
import java.util.Date;
import java.util.Locale;

public class Lesson {
    public String theme;
    public Date date;
    public String address;
    public Date start;
    public Date finish;
    public DocumentReference group;

    public Lesson(){
    }

    public Lesson(Date date, String  address, Date start, Date finish, DocumentReference group, String theme)
    {   this.date = date;
        this.address = address;
        this.start = start;
        this.finish = finish;
        this.group = group;
        this.theme = theme;
    }

}
