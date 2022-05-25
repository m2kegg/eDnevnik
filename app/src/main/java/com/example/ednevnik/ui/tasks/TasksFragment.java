package com.example.ednevnik.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ednevnik.Group;
import com.example.ednevnik.Hometask;
import com.example.ednevnik.Lesson;
import com.example.ednevnik.databinding.FragmentTasksBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {

    private TasksViewModel tasksViewModel;
    private FragmentTasksBinding binding;
    private ArrayList<Group> groups = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel =
                new ViewModelProvider(this).get(TasksViewModel.class);

        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tasksViewModel.getText().observe(getViewLifecycleOwner(), new Observer<List<Hometask>>() {
            @Override
            public void onChanged(List<Hometask> hometasks) {
                ArrayList<String> tasks = new ArrayList<>();
                for (Hometask t:
                     hometasks) {
                    for (Group group:
                            groups) {
                        DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        if(group.admin.getId().equals(reference.getId()) || group.usersToString().contains(FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).getId())){
                            if(!tasks.contains(t.task)){tasks.add(t.task);}
                        }
                    }
                }
                String[] arr = new String[tasks.size()];
                arr = tasks.toArray(arr);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arr);
                ListView listView = binding.lst;
                listView.setAdapter(arrayAdapter);




            }
        });
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
        FirebaseFirestore.getInstance().collection("Groups").get(Source.CACHE).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                groups = (ArrayList<Group>) task.getResult().toObjects(Group.class);
            }
        });
    }
}