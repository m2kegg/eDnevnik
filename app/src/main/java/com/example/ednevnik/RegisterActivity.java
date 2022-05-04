package com.example.ednevnik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText etLogin, etEmail, etPass, etRepeat;
    Button button;
    FirebaseAuth firebaseAuth;
    ProgressDialog pd;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()){
                    pd.show();
                    firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    pd.dismiss();
                                    if (task.isSuccessful()){
                                        switch (radioGroup.getCheckedRadioButtonId()){
                                            case R.id.teacher:
                                                User user = new User(etEmail.getText().toString(), etLogin.getText().toString(), etPass.getText().toString(), true);
                                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).
                                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        });
                                                break;
                                            case R.id.radioButton4:
                                                User user1 = new User(etEmail.getText().toString(), etLogin.getText().toString(), etPass.getText().toString(), false);
                                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user1).
                                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        });
                                                break;
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    void init(){
        radioGroup = findViewById(R.id.TeacherOrSt);
        etLogin = findViewById(R.id.editTextTextPersonName2);
        etEmail = findViewById(R.id.editTextTextEmailAddress);
        etPass = findViewById(R.id.editTextTextPassword2);
        etRepeat = findViewById(R.id.editTextTextPassword3);
        button = findViewById(R.id.button2);
        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pd = new ProgressDialog(getApplicationContext());
        pd.setCancelable(false);
        pd.setTitle("Идёт регистрация");
        pd.setMessage("Пожалуйста, подождите");
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