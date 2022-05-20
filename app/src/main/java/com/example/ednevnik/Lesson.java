package com.example.ednevnik;

import android.location.Address;
import android.text.format.Time;

import org.threeten.bp.LocalDate;
import java.util.Date;
import java.util.Locale;

public class Lesson {
    public LocalDate date;
    public String theme;
    public String address;
    public java.sql.Time start;
    public java.sql.Time finish;
    public Group group;

    public Lesson(){
    }

    public Lesson(LocalDate date, String  address, java.sql.Time start, java.sql.Time finish, Group grup, String theme) {
        this.date = date;
        this.address = address;
        this.start = start;
        this.finish = finish;
        this.group = grup;
        this.theme = theme;
    }

}
