package com.example.ednevnik.ui.tasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ednevnik.POJO.Hometask;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TasksViewModel extends ViewModel {

    private MutableLiveData<List<Hometask>> hometasks;

    public TasksViewModel() {
        hometasks = new MutableLiveData<>();
        List<Hometask> ls = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Tasks").get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot s:
                 task.getResult()) {
                ls.add(s.toObject(Hometask.class));
            }
            hometasks.postValue(ls);

        });
    }

    public LiveData<List<Hometask>> getText() {
        return hometasks;
    }
}