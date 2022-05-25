package com.example.ednevnik.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ednevnik.POJO.Lesson;
import com.example.ednevnik.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LessonView extends AppCompatActivity {
    public CalendarDay day;
    public List<Lesson> lessons = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_view);
        day = getIntent().getParcelableExtra("day");
        Instant instant = day.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
        Date date = DateTimeUtils.toDate(instant);
        Query lesson = FirebaseFirestore.getInstance().collection("Lessons").whereEqualTo("date", date);
        lesson.get().addOnSuccessListener(queryDocumentSnapshots -> {
            lessons = queryDocumentSnapshots.toObjects(Lesson.class);
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setAdapter(new LessonAdapter((ArrayList<Lesson>) lessons));
            recyclerView.setLayoutManager(new LinearLayoutManager(LessonView.this));
        });

    }
}