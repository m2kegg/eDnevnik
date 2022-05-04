package com.example.ednevnik.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ednevnik.Lesson;
import com.example.ednevnik.databinding.FragmentHomeBinding;
import com.google.android.material.datepicker.MaterialCalendar;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final MaterialCalendarView calendarView = binding.calendarView;

        homeViewModel.getLessons().observe(getViewLifecycleOwner(), new Observer<List<Lesson>>() {
            @Override
            public void onChanged(List<Lesson> lessons) {
                calendarView.addDecorator(new LessonDecorator(lessons));
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