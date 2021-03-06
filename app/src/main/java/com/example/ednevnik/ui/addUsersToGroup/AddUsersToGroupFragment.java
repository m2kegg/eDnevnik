package com.example.ednevnik.ui.addUsersToGroup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ednevnik.POJO.Group;
import com.example.ednevnik.R;
import com.example.ednevnik.POJO.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Objects;

public class AddUsersToGroupFragment extends Fragment {
    private Group group1;

    private ArrayList<Group> groups = new ArrayList<>();
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
        Button start = view.findViewById(R.id.addGroup);
        Button students = view.findViewById(R.id.addStudents);
        Button send = view.findViewById(R.id.sendToServer);
        TextView groupTextView = view.findViewById(R.id.chosenGroup);
        TextView studentTextView = view.findViewById(R.id.chosenStudents);
        Source source = Source.CACHE;
        ArrayList<DocumentReference> references = new ArrayList<>();
        ArrayList<User> users1 = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Groups").get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot s:
                    task.getResult()) {
                Group group = s.toObject(Group.class);
                DocumentReference reference = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                if (group.admin.getId().equals(reference.getId()))
                {groups.add(group);}
            }
        });
        start.setOnClickListener(view1 -> {
            FirebaseFirestore.getInstance().collection("Users").get().addOnCompleteListener(task -> {
                for (QueryDocumentSnapshot s:
                        task.getResult()) {
                    User user = s.toObject(User.class);
                    if (!user.isTeacher & !users1.contains(user)) {
                        references.add(FirebaseFirestore.getInstance().document("/Users/" + user.uid));
                        users1.add(user);
                    }
                }
                CharSequence[] names = new CharSequence[groups.size()];
                int n = 0;
                for (Group group: groups){
                    names[n] = group.name;
                    n += 1;
                }
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle(R.string.chose).setSingleChoiceItems(names, -1, (dialogInterface, i) -> group1 = groups.get(i)).setPositiveButton("??????????????", (dialogInterface, i) -> {
                    TextView textView = getView().findViewById(R.id.chosenGroup);
                    textView.setText(group1.name);
                }).create().show();
            });
            students.setOnClickListener(view13 -> {
                boolean[] checkedVariants = new boolean[users1.size()];
                CharSequence[] names = new CharSequence[users1.size()];
                int k = 0;
                for (User user : users1) {
                    names[k] = user.login;
                    k += 1;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.choose_std).setMultiChoiceItems(names, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            checkedVariants[which] = true;
                        }
                    }

                }).setPositiveButton(R.string.choose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int z = 0; z < users1.size(); z++) {
                            if (!checkedVariants[z]) {
                                users1.remove(z);
                                users1.add(z, null);
                                references.remove(z);
                                references.add(z, null);
                            }
                        }
                        users1.removeIf(Objects::isNull);
                        references.removeIf(Objects::isNull);
                        TextView textView = getView().findViewById(R.id.chosenStudents);
                        textView.setText(User.listToString(users1));
                    }
                }).create().show();


            });

        });
        send.setOnClickListener(view12 -> {
            if (references.size() == 0 || users1.size() == 0){
                Toast.makeText(getContext(), R.string.enter_previous, Toast.LENGTH_LONG).show();
            }
            else {
                for (int i = 0; i < references.size(); i++){
                    if (references.get(i) == null) references.remove(i);
                }
                FirebaseFirestore.getInstance().collection("Groups").document(String.valueOf(group1.name)).update("users", references).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getContext(), R.string.success_users, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        return view;
    }
}
