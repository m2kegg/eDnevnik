package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import java.util.Calendar;
import java.util.Date;

public class ScheduleAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setDate(new Date().getTime());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Intent intent = new Intent(ScheduleAct.this, CreateLessonActivity.class);
                intent.putExtra("date", i2+"."+i1+"."+i);
                startActivity(intent);
            }
        });
    }
}