package com.example.ednevnik.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ednevnik.POJO.Lesson;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Lesson>> lessons;


    public HomeViewModel() {
        ArrayList<Lesson> lessonList = new ArrayList<>();
        lessons = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection("Lessons").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot s:
                     task.getResult()) {
                    Lesson lesson = s.toObject(Lesson.class);
                    lessonList.add(lesson);
                }
                if (lessonList != null) lessons.postValue(lessonList);
            }
        });




    }

    public LiveData<List<Lesson>> getLessons() {
        return lessons;
    }
}