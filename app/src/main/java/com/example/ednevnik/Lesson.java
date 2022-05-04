package com.example.ednevnik;

import android.location.Address;
import android.text.format.Time;

import org.threeten.bp.LocalDate;
import java.util.Date;

public class Lesson {
    public LocalDate date;
    public Address address;
    public Time start;
    public Time finish;
    public Group group;

    public Lesson(){
    }

    public Lesson(LocalDate date, Address address, Time start, Time finish, Group grup) {
        this.date = date;
        this.address = address;
        this.start = start;
        this.finish = finish;
        this.group = grup;
    }

}
