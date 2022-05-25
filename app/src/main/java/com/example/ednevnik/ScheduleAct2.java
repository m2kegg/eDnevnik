package com.example.ednevnik;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ednevnik.POJO.Lesson;
import com.example.ednevnik.POJO.User;
import com.example.ednevnik.databinding.ActivityScheduleAct2Binding;
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

import java.util.Date;

public class ScheduleAct2 extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityScheduleAct2Binding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        binding = ActivityScheduleAct2Binding.inflate(getLayoutInflater());
        setSupportActionBar(binding.appBarScheduleAct2.toolbar);
        setContentView(binding.getRoot());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();

        
        // Passing each menu ID as a set of Ids because each
        //menu should be considered as top level destinations.
        //FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
        //@Override
        //public void onDataChange(@NonNull DataSnapshot snapshot) {

        //} else {
        //    mAppBarConfiguration = new AppBarConfiguration.Builder(
        //          R.id.nav_home, R.id.nav_tasks, R.id.nav_navigation, R.id.nav_quit)
        //    .setOpenableLayout(drawer)
        //           .build();
        //}
        //}

        //@Override
        //public void onCancelled(@NonNull DatabaseError error) {

        //}
        //});


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_schedule_act2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void init(){
        Utils _notificationUtils = new Utils(this);
        FirebaseFirestore.getInstance().collection("Lessons").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                long trigger = 1000000000000000000L;
                for (QueryDocumentSnapshot s:
                        task.getResult()) {
                    Lesson lesson = s.toObject(Lesson.class);
                    if (lesson.start.getTime() < trigger & System.currentTimeMillis() < lesson.start.getTime()){
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


                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_home, R.id.nav_addLesson, R.id.nav_addTask, R.id.nav_createGroup, R.id.nav_addUsersToGroup, R.id.nav_tasks, R.id.nav_navigation, R.id.nav_quit)
                        .setOpenableLayout(drawer)
                        .build();
                NavController navController = Navigation.findNavController(ScheduleAct2.this, R.id.nav_host_fragment_content_schedule_act2);
                NavigationUI.setupActionBarWithNavController(ScheduleAct2.this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}