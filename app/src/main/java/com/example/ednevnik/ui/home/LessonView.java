package com.example.ednevnik.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.ednevnik.Group;
import com.example.ednevnik.Lesson;
import com.example.ednevnik.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        lesson.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                lessons = queryDocumentSnapshots.toObjects(Lesson.class);
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setAdapter(new LessonAdapter((ArrayList<Lesson>) lessons));
                recyclerView.setLayoutManager(new LinearLayoutManager(LessonView.this));
            }
        });

    }
}