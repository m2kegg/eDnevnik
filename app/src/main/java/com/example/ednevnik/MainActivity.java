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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        Button btn = findViewById(R.id.btn);
        Button btn2 = findViewById(R.id.button3);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db.collection("Users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Log.i("REGG", "NOW");
                                    User user = documentSnapshot.toObject(User.class);
                                    Log.i("REGG", String.valueOf(user.isTeacher));
                                    if (user.isTeacher) {
                                        startActivity(new Intent(MainActivity.this, ScheduleAct2.class));
                                    } else {
                                        startActivity(new Intent(MainActivity.this, ScheduleAct3.class));
                                    }
                                }
                            });
                        }
                    }
                });
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