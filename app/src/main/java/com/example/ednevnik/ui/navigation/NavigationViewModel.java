package com.example.ednevnik.ui.navigation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ednevnik.POJO.Lesson;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NavigationViewModel extends ViewModel {

    private MutableLiveData<List<Lesson>> lessons;

    public NavigationViewModel() {
        lessons = new MutableLiveData<>();
        ArrayList<Lesson> ls = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Lessons").get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot s:
                    task.getResult()) {
                ls.add(s.toObject(Lesson.class));
            }
            lessons.postValue(ls);
        });

    }

    public LiveData<List<Lesson>> getText() {
        return lessons;
    }
}