package com.example.ednevnik.ui.addUsersToGroup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ednevnik.Group;
import com.example.ednevnik.R;
import com.example.ednevnik.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

public class AddUsersToGroupFragment extends Fragment {
    private Group group1;
    private ArrayList<User> users1 = new ArrayList<>();
    private ArrayList<DocumentReference> references = new ArrayList<>();
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
        FirebaseFirestore.getInstance().collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot s:
                        task.getResult()) {
                    User user = s.toObject(User.class);
                    if (!user.isTeacher) {
                        references.add(FirebaseFirestore.getInstance().document("/Users/" + user.uid));
                        users1.add(user);
                    }
                }
            }
        });
        FirebaseFirestore.getInstance().collection("Groups").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot s:
                        task.getResult()) {
                    Group group = s.toObject(Group.class);
                    groups.add(group);
                }
            }
        });
        users1 = new ArrayList<>();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                            CharSequence[] names = new CharSequence[groups.size()];
                            int n = 0;
                            for (Group group: groups){
                                names[n] = group.name;
                                n += 1;
                            }
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setTitle("Выберите группу").setSingleChoiceItems(names, 1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    group1 = groups.get(i);
                                }
                            }).setPositiveButton("Выбрать", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    TextView textView = getView().findViewById(R.id.chosenGroup);
                                    textView.setText(group1.name);
                                }
                            }).create().show();
                        }
                    });
        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean[] checkedVariants = new boolean[users1.size()];
                CharSequence[] names = new CharSequence[users1.size()];
                int k = 0;
                for (User user : users1) {
                    names[k] = user.login;
                    k += 1;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Выберите учеников").setMultiChoiceItems(names, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            checkedVariants[i] = true;
                        }
                    }
                }).setPositiveButton("Выбрать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int z = 0; z < users1.size(); z++) {
                            if (!checkedVariants[z]) {
                                users1.remove(z);
                                references.remove(z);
                            }
                        }
                        TextView textView = getView().findViewById(R.id.chosenStudents);
                        textView.setText(User.listToString(users1));
                    }
                }).create().show();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (references.size() == 0 || users1.size() == 0){
                    Toast.makeText(getContext(), "Сначала заполните предыдущие поля", Toast.LENGTH_LONG).show();
                }
                else {
                    FirebaseFirestore.getInstance().collection("Groups").document(String.valueOf(group1.name)).update("users", references).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()){
                                Log.i("ERR", task.getException().toString());
                            }
                            else{
                                Log.i("NNN", "SUCCESS");
                            }
                        }
                    });
                }
            }
        });
        return view;
    }
}
