package com.example.fbeyeapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


        if (currentUser != null) {
            String targetID = currentUser.getUid();
            Log.d(TAG,"Current User: " + targetID);
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
                                    Log.d(TAG, "Date: " + date + ", EmployeeUID: " + employeeUid + ", Clock-in Time: " + clockInTime);
                                }
                            }
                        }
                    }
                    else{
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
                                }
                            }
                        }
                    }
                    else{
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