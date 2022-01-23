package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class CreateLessonActivity extends AppCompatActivity {
    EditText etStart, etEnd, etDate;
    Button btn;
    Switch aSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lesson);
        Bundle args = getIntent().getExtras();
        String ChDate = args.get("date").toString();
        init();
        etDate.setText(ChDate);
        btn.setOnClickListener(view -> {
            DateBase db = new DateBase(aSwitch.isActivated(), ChDate, etStart.getText().toString(), etEnd.getText().toString());
            db.save();
            Intent intent = new Intent(CreateLessonActivity.this, ScheduleAct.class);
            startActivity(intent);
        });
    }
    void init(){
        etDate = findViewById(R.id.editTextDate);
        etStart = findViewById(R.id.editTextTime);
        etEnd = findViewById(R.id.editTextTime2);
        btn = findViewById(R.id.button);
        aSwitch = findViewById(R.id.switch1);
    }
}