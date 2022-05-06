package com.example.ednevnik.ui.addUsersToGroup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ednevnik.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddUsersToGroupDialog extends DialogFragment {
    ArrayList<User> users = new ArrayList<>();
    public interface onEndChoiceListener{
        void OnFinishChoose(ArrayList<User> users);
    }
    public static AddUsersToGroupDialog newInstance(String title) {
        AddUsersToGroupDialog frag = new AddUsersToGroupDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s:snapshot.getChildren()){
                    User user = s.getValue(User.class);
                    if (!user.isTeacher){
                        users.add(user);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        boolean[] checkedVariants = new boolean[users.size()];
        ArrayList<String> names = new ArrayList<>();
        for (User user: users){
            names.add(user.login);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ListAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, users);
        builder.setTitle("Выберите учеников").setMultiChoiceItems((CharSequence[]) names.toArray(), null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b){
                    checkedVariants[i] = true;
                }
            }
        }).setPositiveButton("Выбрать", null);
        for (int i = 0; i < users.size(); i++){
            if (!checkedVariants[i]){
                users.remove(i);
            }
        }
        sendBackResult();
        return builder.create();
    }
    public void sendBackResult(){
        onEndChoiceListener endChoiceListener = (onEndChoiceListener) getTargetFragment();
        endChoiceListener.OnFinishChoose(users);
        dismiss();
    }
}
