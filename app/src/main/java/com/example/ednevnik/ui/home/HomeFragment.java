package com.example.ednevnik.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ednevnik.Group;
import com.example.ednevnik.Lesson;
import com.example.ednevnik.ScheduleAct3;
import com.example.ednevnik.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialCalendar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ArrayList<Group> group = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        FirebaseFirestore.getInstance().collection("Groups").get(Source.CACHE).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                group = (ArrayList<Group>) task.getResult().toObjects(Group.class);
            }
        });
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final MaterialCalendarView calendarView = binding.calendarView;
        FirebaseFirestore.getInstance().collection("Groups").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot s:
                     task.getResult()) {
                    group.add(s.toObject(Group.class));
                }
                Log.i("ARR", String.valueOf(group.size()));
                homeViewModel.getLessons().observe(getViewLifecycleOwner(), new Observer<List<Lesson>>() {
                    @Override
                    public void onChanged(List<Lesson> lessons) {
                        calendarView.addDecorator(new LessonDecorator(lessons, group));
                        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                            @Override
                            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                                Intent intent = new Intent(getActivity().getBaseContext(), LessonView.class);
                                intent.putExtra("day", date);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}