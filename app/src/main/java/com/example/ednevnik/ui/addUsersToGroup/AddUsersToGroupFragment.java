package com.example.ednevnik.ui.addUsersToGroup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.ednevnik.Group;
import com.example.ednevnik.R;
import com.example.ednevnik.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddUsersToGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddUsersToGroupFragment extends Fragment implements AddUsersToGroupDialog.onEndChoiceListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private SearchView searchView;
    public AddUsersToGroupFragment() {
        // Required empty public constructor
    }

    public static AddUsersToGroupFragment newInstance(String param1, String param2) {
        AddUsersToGroupFragment fragment = new AddUsersToGroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_users_to_group, container, false);

        return view;
    }

    private void showChooseDialog(){
        FragmentManager fragmentManager = getParentFragmentManager();
        AddUsersToGroupDialog addUsersToGroupDialog = AddUsersToGroupDialog.newInstance("XYZ");
        addUsersToGroupDialog.setTargetFragment(AddUsersToGroupFragment.this, 300);
        addUsersToGroupDialog.show(fragmentManager, null);

    }

    @Override
    public void OnFinishChoose(ArrayList<User> users) {
        FirebaseDatabase.getInstance().getReference("Groups");
    }
}