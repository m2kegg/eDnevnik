package com.example.ednevnik.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ednevnik.POJO.Group;
import com.example.ednevnik.POJO.Lesson;
import com.example.ednevnik.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {
    private ArrayList<Lesson> lessons;


    public LessonAdapter(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

    @NonNull
    @Override
    public LessonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.lesson_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LessonAdapter.ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        TextView leText = holder.lessonText;
        TextView stText = holder.startText;
        TextView enText = holder.endText;
        TextView grText = holder.groupText;
        leText.setText("Тема: " + lesson.theme);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        stText.setText("Начало: " + simpleDateFormat.format(lesson.start));
        enText.setText("Конец: " + simpleDateFormat.format(lesson.finish));
        lesson.group.get().addOnSuccessListener(documentSnapshot -> grText.setText("Группа: " + documentSnapshot.toObject(Group.class).name));
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView lessonText, startText, endText, groupText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonText = itemView.findViewById(R.id.lessonItem);
            startText = itemView.findViewById(R.id.dataItem);
            endText = itemView.findViewById(R.id.textView4);
            groupText = itemView.findViewById(R.id.groupItem);
        }
    }
}
