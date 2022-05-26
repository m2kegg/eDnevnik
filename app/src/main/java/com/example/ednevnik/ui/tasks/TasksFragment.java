package com.example.ednevnik.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.ednevnik.POJO.Group;
import com.example.ednevnik.POJO.Hometask;
import com.example.ednevnik.databinding.FragmentTasksBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

public class TasksFragment extends Fragment {

    private TasksViewModel tasksViewModel;
    private FragmentTasksBinding binding;
    private ArrayList<Group> groups = new ArrayList<>();
    private ArrayList<Hometask> hometasks = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel =
                new ViewModelProvider(this).get(TasksViewModel.class);

        binding = FragmentTasksBinding.inflate(inflater, container, false);
        ArrayList<String> tasks = new ArrayList<>();
        for (Hometask t :
                hometasks) {
            for (Group group :
                    groups) {
                DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                if (group.admin.getId().equals(reference.getId()) || group.usersToString().contains(FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).getId())) {
                    if (!tasks.contains(t.task)) {
                        tasks.add(t.task);
                    }
                }
            }
        }
        String[] arr = new String[tasks.size()];
        arr = tasks.toArray(arr);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arr);
        ListView listView = binding.lst;
        listView.setAdapter(arrayAdapter);

        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore.getInstance().collection("Tasks").get(Source.CACHE).addOnCompleteListener(task -> hometasks = (ArrayList<Hometask>) task.getResult().toObjects(Hometask.class));
        FirebaseFirestore.getInstance().collection("Groups").get(Source.CACHE).addOnCompleteListener(task -> groups = (ArrayList<Group>) task.getResult().toObjects(Group.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseFirestore.getInstance().collection("Groups").get().addOnCompleteListener(task -> groups = (ArrayList<Group>) task.getResult().toObjects(Group.class));
        FirebaseFirestore.getInstance().collection("Tasks").get().addOnCompleteListener(task -> hometasks = (ArrayList<Hometask>) task.getResult().toObjects(Hometask.class));
            ArrayList<String> tasks = new ArrayList<>();
            for (Hometask t :
                    hometasks) {
                for (Group group :
                        groups) {
                    DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if (group.admin.getId().equals(reference.getId()) || group.usersToString().contains(FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).getId())) {
                        if (!tasks.contains(t.task)) {
                            tasks.add(t.task);
                        }
                    }
                }
            }
            String[] arr = new String[tasks.size()];
            arr = tasks.toArray(arr);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arr);
            ListView listView = binding.lst;
            listView.setAdapter(arrayAdapter);
    }
}