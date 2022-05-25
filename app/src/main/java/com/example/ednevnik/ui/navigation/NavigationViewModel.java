package com.example.ednevnik.ui.navigation;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ednevnik.Lesson;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NavigationViewModel extends ViewModel {

    private MutableLiveData<List<Lesson>> lessons;

    public NavigationViewModel() {
        lessons = new MutableLiveData<>();
        ArrayList<Lesson> ls = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Lessons").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot s:
                        task.getResult()) {
                    ls.add(s.toObject(Lesson.class));
                }
                lessons.postValue(ls);
            }
        });

    }

    public LiveData<List<Lesson>> getText() {
        return lessons;
    }
}