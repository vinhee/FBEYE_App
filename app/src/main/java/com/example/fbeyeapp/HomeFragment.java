package com.example.fbeyeapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    Button checkAttendance;
    Button applyMCLeave;
    Button applyLeave;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        checkAttendance = view.findViewById(R.id.checkAttendanceBtn);
        applyMCLeave = view.findViewById(R.id.applyMcBtn);
        applyLeave = view.findViewById(R.id.applyLeaveBtn);



        checkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attendance = new Intent(getActivity(), checkAttendance.class);
                startActivity(attendance);
            }
        });

        applyMCLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent apply = new Intent(getActivity(), applyLeave.class);
                startActivity(apply);
            }
        });

        applyLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent apply = new Intent(getActivity(), applyLeaveRequest.class);
                startActivity(apply);
            }
        });

        return view;
    }
}