package com.example.ednevnik.ui.quit;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ednevnik.MainActivity;
import com.example.ednevnik.R;
import com.google.firebase.auth.FirebaseAuth;


public class QuitFragment extends Fragment {

    private String mParam1;
    private String mParam2;

    public QuitFragment() {
        // Required empty public constructor
    }

    public static QuitFragment newInstance(String param1, String param2) {
        QuitFragment fragment = new QuitFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quit, container, false);
        Button button = view.findViewById(R.id.button9);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getBaseContext(), MainActivity.class));
            }
        });
        return view;
    }
}