package com.example.ednevnik.ui.addUsersToGroup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.ednevnik.Group;
import com.example.ednevnik.R;
import com.example.ednevnik.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddUsersToGroupFragment extends Fragment implements AddUsersToGroupDialog.onEndChoiceListener, ChooseGroupDialog.onEndChoiceListener {
    Group group1;
    ArrayList<User> users1;
    public AddUsersToGroupFragment() {
        // Required empty public constructor
    }

    public static AddUsersToGroupFragment newInstance(String param1, String param2) {
        AddUsersToGroupFragment fragment = new AddUsersToGroupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_users_to_group, container, false);
        Button addGroup = view.findViewById(R.id.button4);
        Button addUsers = view.findViewById(R.id.button6);
        Button send = view.findViewById(R.id.button7);
        TextView groupTextView = view.findViewById(R.id.chosenGroup);
        TextView studentTextView = view.findViewById(R.id.chosenStudents);
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGroupChooseDialog();
                groupTextView.setText(group1.name);
            }
        });
        addUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserChooseDialog();
                studentTextView.setText(User.listToString(users1));
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                group1.users = users1;
                FirebaseDatabase.getInstance().getReference("Groups").child(String.valueOf(group1.hashCode())).setValue(group1);
            }
        });
        return view;
    }

    private void showUserChooseDialog(){
        FragmentManager fragmentManager = getParentFragmentManager();
        AddUsersToGroupDialog addUsersToGroupDialog = AddUsersToGroupDialog.newInstance("XYZ");
        addUsersToGroupDialog.setTargetFragment(AddUsersToGroupFragment.this, 300);
        addUsersToGroupDialog.show(fragmentManager, null);

    }
    private void showGroupChooseDialog(){
        FragmentManager fragmentManager = getParentFragmentManager();
        ChooseGroupDialog chooseGroupDialog  = ChooseGroupDialog.newInstance("XYZ");
        chooseGroupDialog.setTargetFragment(AddUsersToGroupFragment.this, 300);
        chooseGroupDialog.show(fragmentManager, null);
    }

    @Override
    public void OnFinishChoose(ArrayList<User> users) {
        users1 = users;
    }

    @Override
    public void OnFinishChoose(Group group) {
        group1 = group;
    }
}