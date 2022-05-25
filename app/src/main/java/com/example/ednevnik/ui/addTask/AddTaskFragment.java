package com.example.ednevnik.ui.addTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ednevnik.Group;
import com.example.ednevnik.Hometask;
import com.example.ednevnik.Lesson;
import com.example.ednevnik.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        FirebaseFirestore.getInstance().collection("Lessons").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot s:
                     task.getResult()) {
                    groups.add(s.toObject(Group.class));
                }
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] names = new CharSequence[groups.size()];
                int n = 0;
                for (Group group: groups){
                    names[n] = group.name;
                    n += 1;
                }
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Выберите группу").setSingleChoiceItems(names, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        group1 = groups.get(i);
                    }
                }).setPositiveButton("Выбрать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextView textView = getView().findViewById(R.id.textView14);
                        textView.setText(group1.name);
                    }
                }).create().show();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hometask hometask = new Hometask(task.getText().toString(), FirebaseFirestore.getInstance().collection("Lessons").document(group1.name));
                FirebaseFirestore.getInstance().collection("Tasks").add(hometask).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Домашнее задание успешно добавлено", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getContext(), task.getException().getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        return view;
    }
}