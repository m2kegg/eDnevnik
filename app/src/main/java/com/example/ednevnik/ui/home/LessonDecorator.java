package com.example.ednevnik.ui.home;

import android.graphics.Color;

import com.example.ednevnik.POJO.Group;
import com.example.ednevnik.POJO.Lesson;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.threeten.bp.LocalDate;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class LessonDecorator implements DayViewDecorator {
    private ArrayList<CalendarDay>  calendarDays = new ArrayList<>();
    private Group group1;
    private int color;

    public LessonDecorator(List<Lesson> lessons) {
        this.color = Color.CYAN;
        for (Lesson lesson: lessons){
            LocalDate localDate = LocalDate.ofEpochDay(Instant.ofEpochMilli(lesson.date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay());
            CalendarDay day = CalendarDay.from(localDate);
                    calendarDays.add(day);
        }
    }
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return calendarDays.contains(day);
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.addSpan(new DotSpan(5, color));
            }
        }
