package com.example.ednevnik.ui.createGroup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ednevnik.POJO.Group;
import com.example.ednevnik.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateGroupFragment extends Fragment {

    public CreateGroupFragment() {
        // Required empty public constructor
    }

    public static CreateGroupFragment newInstance(String param1, String param2) {
        CreateGroupFragment fragment = new CreateGroupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);
        EditText textView = view.findViewById(R.id.editTextTextPersonName3);
        Button button = view.findViewById(R.id.button5);
        button.setOnClickListener(view1 -> {
            if (!textView.getText().toString().equals("")) {
                Group group = new Group(textView.getText().toString(), FirebaseFirestore.getInstance().document("/Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()));
                FirebaseFirestore.getInstance().collection("Groups").document(group.name).set(group).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), R.string.group_success, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return view;
    }
}