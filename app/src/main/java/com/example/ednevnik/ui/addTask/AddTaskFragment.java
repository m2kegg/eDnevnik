package com.example.ednevnik.ui.addTask;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ednevnik.POJO.Group;
import com.example.ednevnik.POJO.Hometask;
import com.example.ednevnik.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class AddTaskFragment extends Fragment {
    private ArrayList<Group> groups = new ArrayList<>();
    private Group group1;

    public AddTaskFragment() {
        // Required empty public constructor
    }


    public static AddTaskFragment newInstance(String param1, String param2) {
        AddTaskFragment fragment = new AddTaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        FirebaseFirestore.getInstance().collection("Groups").get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot s:
                 task.getResult()) {
                groups.add(s.toObject(Group.class));
            }
        });
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_task, container, false);
        EditText task = view.findViewById(R.id.editTextTextPersonName4);
        Button button = view.findViewById(R.id.button4);
        Button button1 = view.findViewById(R.id.button6);
        button.setOnClickListener(v -> {
            CharSequence[] names = new CharSequence[groups.size()];
            int n = 0;
            for (Group group: groups){
                names[n] = group.name;
                n += 1;
            }
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setTitle(R.string.chooseGroups).setSingleChoiceItems(names, -1, (dialogInterface, i) -> group1 = groups.get(i)).setPositiveButton(R.string.choose, (dialogInterface, i) -> {
                TextView textView = getView().findViewById(R.id.textView14);
                textView.setText(group1.name);
            }).create().show();
        });
        button1.setOnClickListener(v -> {
            if (check()) {
                Hometask hometask = new Hometask(task.getText().toString(), FirebaseFirestore.getInstance().collection("Lessons").document(group1.name));
                FirebaseFirestore.getInstance().collection("Tasks").add(hometask).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(getContext(), R.string.success_task, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), task1.getException().getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return view;
    }
    private boolean check(){
        EditText task = getView().findViewById(R.id.editTextTextPersonName4);
        if (task.getText().toString().equals("")){
            task.setError(getString(R.string.entertext));
            return false;
        }
        if (group1 == null){
            Toast.makeText(getContext(), R.string.dialog, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}