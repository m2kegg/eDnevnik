package com.example.ednevnik.ui.home;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ednevnik.Lesson;
import com.example.ednevnik.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.ArrayList;
import java.util.List;

public class LessonDecorator implements DayViewDecorator {
    private final ArrayList<CalendarDay>  calendarDays = new ArrayList<>();
    private final int color;

    public LessonDecorator(List<Lesson> lessons) {
        this.color = Color.CYAN;
        for (Lesson lesson: lessons){
            CalendarDay day = CalendarDay.from(lesson.date);
            calendarDays.add(day);
        }
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return calendarDays.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {
                canvas.drawColor(color);
            }

            @Override
            public void setAlpha(int i) {

            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.OPAQUE;
            }
        });
    }
}
