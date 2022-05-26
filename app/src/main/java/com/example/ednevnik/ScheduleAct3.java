package com.example.ednevnik;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ednevnik.POJO.Lesson;
import com.example.ednevnik.POJO.User;
import com.example.ednevnik.databinding.ActivityScheduleAct3Binding;
import com.example.ednevnik.notification.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ScheduleAct3 extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityScheduleAct3Binding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Utils _notificationUtils = new Utils(this);
        FirebaseFirestore.getInstance().collection("Lessons").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                long trigger = 1000000000000000000L;
                for (QueryDocumentSnapshot s :
                        task.getResult()) {
                    Lesson lesson = s.toObject(Lesson.class);
                    if (lesson.start.getTime() < trigger & System.currentTimeMillis() < lesson.start.getTime()) {
                        trigger = lesson.start.getTime();
                    }
                }
                long twoHours = 1000 * 60 * 60 * 2;
                long time = trigger - twoHours;
                _notificationUtils.setReminder(time);
                DrawerLayout drawer = binding.drawerLayout;
                //User user = snapshot.getValue(User.class);
                //if (user.isTeacher) {
                NavigationView navigationView = binding.navView;
                View hView = navigationView.getHeaderView(0);
                TextView nav_user = hView.findViewById(R.id.user);
                TextView nav_teacher = hView.findViewById(R.id.teacger);
                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User curuser = task.getResult().toObject(User.class);
                        nav_user.setText(curuser.login);
                        nav_teacher.setText(curuser.isTeacher ? "Учитель" : "Ученик");
                    }
                });
                binding = ActivityScheduleAct3Binding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());
                setSupportActionBar(binding.appBarScheduleAct3.toolbar);
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_home, R.id.nav_tasks, R.id.nav_navigation, R.id.nav_quit)
                        .setOpenableLayout(drawer)
                        .build();
                NavController navController = Navigation.findNavController(ScheduleAct3.this, R.id.nav_host_fragment_content_schedule_act3);
                NavigationUI.setupActionBarWithNavController(ScheduleAct3.this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);
            }
    });
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_schedule_act3);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}