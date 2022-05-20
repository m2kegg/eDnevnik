package com.example.ednevnik.ui.addLesson;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ednevnik.Group;
import com.example.ednevnik.Lesson;
import com.example.ednevnik.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.threeten.bp.LocalDate;

import java.sql.Time;
import java.util.ArrayList;


public class AddLessonFragment extends Fragment {
    EditText theme, start, finish, address, date;
    Button choose, add;
    ArrayList<Group> groups = new ArrayList<>();
    Group group1;
    public AddLessonFragment() {
        // Required empty public constructor
    }

    public static AddLessonFragment newInstance(String param1, String param2) {
        AddLessonFragment fragment = new AddLessonFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_lesson, container, false);
        init(view);
        choose.setOnClickListener(new View.OnClickListener() {
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()){
                    String date1 = date.getText().toString();
                    LocalDate localDate = LocalDate.parse(date1);
                    Lesson lesson = new Lesson(localDate, address.getText().toString(), Time.valueOf(start.getText().toString()), Time.valueOf(finish.getText().toString()), group1, theme.getText().toString() );
                }
            }

        });
        return view;
    }
    private void init(View view){
        theme = view.findViewById(R.id.editTextTextPersonName4);
        start = view.findViewById(R.id.editTextTime3);
        finish = view.findViewById(R.id.editTextTime5);
        address = view.findViewById(R.id.editTextTextPostalAddress2);
        date = view.findViewById(R.id.editTextDate2);
        choose = view.findViewById(R.id.button4);
        add = view.findViewById(R.id.button6);
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
    }
    private boolean check(){
        if (theme.getText().toString().equals("")){
            theme.setError("Пожалуйста, введите тему");
            return false;
        }
        if (start.getText().toString().equals("")){
            theme.setError("Пожалуйста, введите время начала занятия");
            return false;
        }
        if (finish.getText().toString().equals("")){
            theme.setError("Пожалуйста, введите окончания занятия");
            return false;
        }
        if (address.getText().toString().equals("")){
            theme.setError("Пожалуйста, введите место занятия");
            return false;
        }
        if (date.getText().toString().equals("")){
            theme.setError("Пожалуйста, введите дату занятия");
            return false;
        }
        return true;
    }
}