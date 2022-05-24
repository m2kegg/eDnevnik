package com.example.ednevnik;

import android.location.Address;
import android.text.format.Time;

import com.google.firebase.firestore.DocumentReference;

import org.threeten.bp.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Lesson {
    public String theme;
    public Date date;
    public Map<String, Object> address;
    public Date start;
    public Date finish;
    public DocumentReference group;

    public Lesson(){
    }

    public Lesson(Date date, Map<String, Object>  address, Date start, Date finish, DocumentReference group, String theme)
    {   this.date = date;
        this.address = address;
        this.start = start;
        this.finish = finish;
        this.group = group;
        this.theme = theme;
    }

}
