package com.example.ednevnik.ui.home;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ednevnik.Group;
import com.example.ednevnik.Lesson;
import com.example.ednevnik.R;
import com.google.firebase.analytics.FirebaseAnalytics;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LessonDecorator implements DayViewDecorator {
    private ArrayList<CalendarDay>  calendarDays = new ArrayList<>();
    private Group group1;
    private int color;

    public LessonDecorator(List<Lesson> lessons, ArrayList<Group> groups) {
        this.color = Color.CYAN;
        for (Lesson lesson: lessons){
            LocalDate localDate = LocalDate.ofEpochDay(Instant.ofEpochMilli(lesson.date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay());
            CalendarDay day = CalendarDay.from(localDate);
            Log.d("CAL", String.valueOf(day.getDate().toString()));
            for (Group group:
                    groups ) {
                Log.i("ADM1", group.admin.toString());
                Log.i("ADM2", FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).toString() );
                DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                if(group.admin.getId().equals(reference.getId()) || group.usersToString().contains(FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).getId())){
                    calendarDays.add(day);
                }
            }
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
