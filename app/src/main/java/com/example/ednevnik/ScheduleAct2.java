package com.example.ednevnik;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ednevnik.databinding.ActivityScheduleAct2Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScheduleAct2 extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityScheduleAct2Binding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        binding = ActivityScheduleAct2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarScheduleAct2.toolbar);
        binding.appBarScheduleAct2.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /* FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.isTeacher){ */
                    mAppBarConfiguration = new AppBarConfiguration.Builder(
                            R.id.nav_home, R.id.nav_addLesson, R.id.nav_addTask, R.id.nav_createGroup, R.id.nav_addUsersToGroup, R.id.nav_tasks, R.id.nav_navigation, R.id.nav_quit)
                            .setOpenableLayout(drawer)
                            .build();
                //}
                /*else {
                    mAppBarConfiguration = new AppBarConfiguration.Builder(
                            R.id.nav_home, R.id.nav_tasks, R.id.nav_navigation, R.id.nav_quit)
                            .setOpenableLayout(drawer)
                            .build();
                }
            }*/

           /* @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); */

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_schedule_act2);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.schedule_act2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_schedule_act2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}