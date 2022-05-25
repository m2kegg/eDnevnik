package com.example.ednevnik.ui.tasks;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ednevnik.Hometask;
import com.example.ednevnik.Lesson;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TasksViewModel extends ViewModel {

    private MutableLiveData<List<Hometask>> hometasks;

    public TasksViewModel() {
        hometasks = new MutableLiveData<>();
        List<Hometask> ls = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Tasks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot s:
                     task.getResult()) {
                    ls.add(s.toObject(Hometask.class));
                }
                hometasks.postValue(ls);

            }
        });
    }

    public LiveData<List<Hometask>> getText() {
        return hometasks;
    }
}