package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText login, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button btn = findViewById(R.id.btn);
        Button btn2 = findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()){
                    switch (radioGroup.getCheckedRadioButtonId()){
                        case R.id.radioButton2:
                            Toast.makeText(getApplicationContext(), "WIP", Toast.LENGTH_LONG).show();
                            break;
                        case R.id.radioButton3:
                            Intent intent = new Intent(MainActivity.this, ScheduleAct.class);
                            startActivity(intent);
                    }
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private boolean check(){
        List<UsersDatabase> usersDB = UsersDatabase.find(UsersDatabase.class, "login = ? and pass = ?", login.getText().toString(), password.getText().toString());
        if (usersDB.isEmpty()){
            login.setError("This user does not exist");
            return false;
        }
        if (password.getText().length() < 8){
            password.setError("Enter the correct password");
            return false;
        }

        return true;
    }
}