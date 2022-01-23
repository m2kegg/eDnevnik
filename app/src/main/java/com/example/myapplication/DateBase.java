package com.example.myapplication;

import com.orm.SugarRecord;

public class DateBase extends SugarRecord {

    Boolean isDistant;
    String date;
    String start;
    String end;

    public DateBase() {
    }

    public DateBase(Boolean isDistant, String date, String start, String end) {
        this.isDistant = isDistant;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return
                "Дистанционно: " + (isDistant ? "да" : "нет") +
                ", дата занятия: " + date + ' ' +
                ", начало занятия: " + start + ' ' +
                ", конец занятия: " + end + "\n";
    }
}
