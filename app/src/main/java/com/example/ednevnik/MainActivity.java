package com.example.ednevnik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText email, password;

    @Override
    protected void onStart() {
        super.onStart();
        //if (auth.getCurrentUser() != null){
            //заход в приложение
        //}
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        Button btn = findViewById(R.id.btn);
        Button btn2 = findViewById(R.id.button3);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       if (check()) {
                                           FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                   ArrayList<User> users = new ArrayList<>();
                                                   for (DataSnapshot s:
                                                        snapshot.getChildren()) {
                                                       User user = s.getValue(User.class);
                                                       users.add(user);
                                                   }
                                                   for (int i = 0; i < users.size(); i++){
                                                       if (users.get(i).email == email.getText().toString()){
                                                           if (users.get(i).isTeacher){
                                                               Intent intent = new Intent(MainActivity.this, ScheduleAct2.class);
                                                               startActivity(intent);
                                                           }
                                                           else{
                                                               Intent intent = new Intent(MainActivity.this, ScheduleAct3.class);
                                                               startActivity(intent);
                                                           }
                                                       }
                                                   }
                                               }

                                               @Override
                                               public void onCancelled(@NonNull DatabaseError error) {

                                               }
                                           });
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

            private boolean check() {
                if (password.getText().length() < 8) {
                    password.setError("Enter the correct password");
                    return false;
                }
                return true;
            }
        }