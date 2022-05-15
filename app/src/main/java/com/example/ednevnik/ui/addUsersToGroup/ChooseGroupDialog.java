package com.example.ednevnik.ui.addUsersToGroup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ednevnik.Group;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseGroupDialog extends DialogFragment {
    ArrayList<Group> groups = new ArrayList<>();
    Group group1;
    public interface onEndChoiceListener{
        void OnFinishChoose(Group group);
    }
    public static ChooseGroupDialog newInstance(String title) {
        ChooseGroupDialog frag = new ChooseGroupDialog();
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
                    Group group = s.getValue(Group.class);
                        groups.add(group);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        boolean[] checkedVariants = new boolean[groups.size()];
        ArrayList<String> names = new ArrayList<>();
        for (Group group: groups){
            names.add(group.name);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выберите группу");
        builder.setSingleChoiceItems((CharSequence[]) names.toArray(), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                group1 = groups.get(i);
            }
        }).setPositiveButton("Выбрать", null);
        sendBackResult();
        return builder.create();
    }
    public void sendBackResult(){
        onEndChoiceListener endChoiceListener = (onEndChoiceListener) getTargetFragment();
        endChoiceListener.OnFinishChoose(group1);
        dismiss();
    }
}