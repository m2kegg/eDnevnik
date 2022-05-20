package com.example.ednevnik.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ednevnik.Lesson;
import com.example.ednevnik.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Lesson>> lessons;
    private DatabaseReference mDatabase;

    public HomeViewModel() {
        lessons = new MutableLiveData<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        List<Lesson> lessonList = new ArrayList<>();
        final User[] users = new User[1];
        if (lessonList != null) lessons.postValue(lessonList);

    }

    public LiveData<List<Lesson>> getLessons() {
        return lessons;
    }
}