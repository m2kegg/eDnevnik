package com.example.ednevnik.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.ednevnik.POJO.Group;
import com.example.ednevnik.POJO.Lesson;
import com.example.ednevnik.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private MaterialCalendarView calendarView;

    private FragmentHomeBinding binding;
    private static ArrayList<Lesson> lessons = new ArrayList<>();
    private Group group1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        calendarView = binding.calendarView;
        FirebaseFirestore.getInstance().collection("Lessons").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot s:
                        task.getResult()) {
                    Lesson lesson = s.toObject(Lesson.class);
                    lesson.group.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Group group = task.getResult().toObject(Group.class);
                            if (group.usersToString().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()) || group.admin.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                calendarView.addDecorator(new LessonDecorator(lessons));
                                calendarView.setOnDateChangedListener((widget, date, selected) -> {
                                    Intent intent = new Intent(getActivity().getBaseContext(), LessonView.class);
                                    intent.putExtra("day", date);
                                    startActivity(intent);
                                });
                            }
                        }

                    });
                    calendarView.setOnDateChangedListener((widget, date, selected) -> {
                        Intent intent = new Intent(getActivity().getBaseContext(), LessonView.class);
                        intent.putExtra("day", date);
                        startActivity(intent);
                    });
                }
            }
        });



                return root;
            }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        FirebaseFirestore.getInstance().collection("Lessons").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot s :
                        task.getResult()) {
                    Lesson lesson = s.toObject(Lesson.class);
                    lesson.group.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Group group = task.getResult().toObject(Group.class);
                            if (group.usersToString().contains(FirebaseAuth.getInstance().getCurrentUser().getUid()) || group.admin.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                calendarView.removeDecorators();
                                calendarView.addDecorator(new LessonDecorator(lessons));
                                calendarView.setOnDateChangedListener((widget, date, selected) -> {
                                    Intent intent = new Intent(getActivity().getBaseContext(), LessonView.class);
                                    intent.putExtra("day", date);
                                    startActivity(intent);
                                });
                            }
                        }

                    });
                    calendarView.setOnDateChangedListener((widget, date, selected) -> {
                        Intent intent = new Intent(getActivity().getBaseContext(), LessonView.class);
                        intent.putExtra("day", date);
                        startActivity(intent);
                    });
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore.getInstance().collection("Lessons").get().addOnCompleteListener(task -> lessons = (ArrayList<Lesson>) task.getResult().toObjects(Lesson.class));
    }
}