package com.example.ednevnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null){
            //заход в приложение
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button btn = findViewById(R.id.btn);
        Button btn2 = findViewById(R.id.button3);
        auth = FirebaseAuth.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()){
                    switch (radioGroup.getCheckedRadioButtonId()){
                        case R.id.radioButton2:
                            Toast.makeText(getApplicationContext(), "WIP", Toast.LENGTH_LONG).show();
                            break;
                        case R.id.radioButton3:
                            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString());
                            Intent intent = new Intent(MainActivity.this, ScheduleAct2.class);
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
        if (password.getText().length() < 8){
            password.setError("Enter the correct password");
            return false;
        }

        return true;
    }
}