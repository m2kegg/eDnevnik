package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.CalendarView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
                intent.putExtra("date", i2+"."+(i1 + 1)+"."+i);
                startActivity(intent);
            }
        });
        TextView textView = findViewById(R.id.textView8);
        textView.setMovementMethod(new ScrollingMovementMethod());
        List<DateBase> dates = DateBase.listAll(DateBase.class);
        textView.setText(dates.toString().replace("[" , "").replace("]", ""));
    }
}