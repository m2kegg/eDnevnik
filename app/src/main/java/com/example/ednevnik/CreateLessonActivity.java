package com.example.ednevnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class CreateLessonActivity extends AppCompatActivity {
    EditText etStart, etEnd, etDate;
    Button btn;
    Switch aSwitch;
    int switched;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lesson);
        Bundle args = getIntent().getExtras();
        String ChDate = args.get("date").toString();
        init();
        etDate.setText(ChDate);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switched = 1;
                }
                else {
                    switched = 0;
                }
            }
        });
        btn.setOnClickListener(view -> {
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