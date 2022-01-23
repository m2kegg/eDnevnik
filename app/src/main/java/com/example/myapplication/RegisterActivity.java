package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    EditText etLogin, etEmail, etPass, etRepeat;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()){
                    UsersDatabase usersDatabase = new UsersDatabase(etEmail.getText().toString() ,etLogin.getText().toString(), etPass.getText().toString());
                    usersDatabase.save();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    void init(){
        etLogin = findViewById(R.id.editTextTextPersonName2);
        etEmail = findViewById(R.id.editTextTextEmailAddress);
        etPass = findViewById(R.id.editTextTextPassword2);
        etRepeat = findViewById(R.id.editTextTextPassword3);
        button = findViewById(R.id.button2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    boolean check(){
        if (etLogin.getText().toString().equals("")){
            etLogin.setError("Enter name");
            return false;
        }
        if (etEmail.getText().toString().equals("") || !Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
            etEmail.setError("Enter correct email");
            return false;
        }
        if (etPass.getText().toString().length() < 8 || etPass.getText().toString().equals("")){
            etPass.setError("Enter correct password");
            return false;
        }
        if (!etPass.getText().toString().equals(etRepeat.getText().toString())){
            etRepeat.setError("Passwords don't match");
            return false;
        }
        return true;

    }
}