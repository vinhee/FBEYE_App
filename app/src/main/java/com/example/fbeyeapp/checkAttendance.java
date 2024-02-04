package com.example.fbeyeapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.media.RouteListingPreference;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class checkAttendance extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference Employeereference;
    DatabaseReference clockInReference;
    DatabaseReference clockOutReference;
    DatabaseReference leaveReference;

    FirebaseAuth mAuth;

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance);

        mAuth = FirebaseAuth.getInstance();
        Employeereference = FirebaseDatabase.getInstance().getReference("Employees");
        clockInReference = FirebaseDatabase.getInstance().getReference("ClockInLog");
        clockOutReference = FirebaseDatabase.getInstance().getReference("ClockOutLog");
        leaveReference = FirebaseDatabase.getInstance().getReference("EmployeeLeave");
        currentUser = mAuth.getCurrentUser();
        RecyclerView recyclerView = findViewById(R.id.attendanceRecyclerView);

        ArrayList<employeeAttendance> employeeAttendance = new ArrayList<>();
        ArrayList clockInList = new ArrayList<>();
        ArrayList clockOutList = new ArrayList<>();
        ArrayList DateList = new ArrayList<>();

        if (currentUser != null) {
            String targetID = currentUser.getUid();
            Log.d(TAG, "Current User: " + targetID);
            clockInReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                            String date = dateSnapshot.getKey();
                            for (DataSnapshot attendanceSnapshot : dateSnapshot.getChildren()) {
                                String employeeUid = attendanceSnapshot.getKey();
                                if (employeeUid.equals(targetID)) {
                                    String clockInTime = attendanceSnapshot.getValue(String.class);
                                    DateList.add(date);
                                    clockInList.add(clockInTime);
                                }
                            }
                        }
                        // No need to do anything here. Wait for clockOutReference onDataChange.
                    } else {
                        Log.d(TAG, "No attendance data found.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            clockOutReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                            String date = dateSnapshot.getKey();
                            for (DataSnapshot attendanceSnapshot : dateSnapshot.getChildren()) {
                                String employeeUid = attendanceSnapshot.getKey();
                                if (employeeUid.equals(targetID)) {
                                    String clockOutTime = attendanceSnapshot.getValue(String.class);
                                    Log.d(TAG, "Date: " + date + ", EmployeeUID: " + employeeUid + ", Clock-Out Time: " + clockOutTime);
                                    clockOutList.add(clockOutTime);
                                }
                            }
                        }

                        for (int i = 0; i < Math.min(DateList.size(), Math.min(clockInList.size(), clockOutList.size())); i++) {
                            Object date = DateList.get(i);
                            Object clockInTime = clockInList.get(i);
                            Object clockOutTime = clockOutList.get(i);
                            employeeAttendance.add(new employeeAttendance(date, clockInTime, clockOutTime));
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(checkAttendance.this));
                        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), employeeAttendance));
                    } else {
                        Log.d(TAG, "No attendance data found.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}