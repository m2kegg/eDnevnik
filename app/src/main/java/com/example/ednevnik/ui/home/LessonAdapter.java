package com.example.ednevnik.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ednevnik.Group;
import com.example.ednevnik.Lesson;
import com.example.ednevnik.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

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
        TextView daText = holder.dataText;
        TextView grText = holder.groupText;
        leText.setText(lesson.theme);
        daText.setText(lesson.date.toString());
        lesson.group.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                grText.setText(documentSnapshot.toObject(Group.class).name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView lessonText, dataText, groupText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonText = itemView.findViewById(R.id.lessonItem);
            dataText = itemView.findViewById(R.id.dataItem);
            groupText = itemView.findViewById(R.id.groupItem);
        }
    }
}
