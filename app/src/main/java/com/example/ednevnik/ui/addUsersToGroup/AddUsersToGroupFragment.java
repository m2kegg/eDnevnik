package com.example.ednevnik.ui.addUsersToGroup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
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
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s:
                     snapshot.getChildren()) {
                    User user = s.getValue(User.class);
                    if (!user.isTeacher)
                    users1.add(user);
                }
                boolean[] checkedVariants = new boolean[users1.size()];
                ArrayList<String> names = new ArrayList<>();
                for (User user: users1){
                    names.add(user.login);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Выберите учеников").setMultiChoiceItems((CharSequence[]) names.toArray(), null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b){
                            checkedVariants[i] = true;
                        }
                    }
                }).setPositiveButton("Выбрать", null).show();
                for (int i = 0; i < users1.size(); i++){
                    if (!checkedVariants[i]){
                        users1.remove(i);
                    }
                }
                TextView studentTextView = getView().findViewById(R.id.chosenStudents);
                studentTextView.setText(User.listToString(users1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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