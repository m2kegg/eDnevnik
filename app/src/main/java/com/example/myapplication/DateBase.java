package com.example.myapplication;

import com.orm.SugarRecord;

public class DateBase extends SugarRecord {

    int isDistant;
    String date;
    String start;
    String end;

    public DateBase() {
    }

    public DateBase(int isDistant, String date, String start, String end) {
        this.isDistant = isDistant;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return
                "Дистанционно: " + (isDistant == 1 ? "да" : "нет") +
                ", дата занятия: " + date + ' ' +
                ", начало занятия: " + start + ' ' +
                ", конец занятия: " + end + "\n";
    }
}
