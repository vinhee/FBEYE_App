package com.example.fbeyeapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class applyLeaveRequest extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    Button startDateBtn;
    Button endDateBtn;
    Button ApplyLeave;
    TextView absenceReason;

    Button submitBtn;
    EditText leaveReason;
    String department;
    // Separate variables for start and end dates
    String startDate;
    String endDate;
    Long employeeId;
    String employeeName;
    // Variable to store the currently selected button
    private Button selectedButton;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave_request);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        startDateBtn = findViewById(R.id.startDateButton);
        endDateBtn = findViewById(R.id.endDateButton);
        submitBtn = findViewById(R.id.submitButton);
        absenceReason = findViewById(R.id.AbsenceReason);
        leaveReason = findViewById(R.id.leaveReason);

        startDateBtn.setText(getTodaysDate());
        endDateBtn.setText(getTodaysDate()); // Initialize end date with today's date
        initDatePicker();

        String userID = currentUser.getUid();
        DatabaseReference employeeRef = databaseReference.child("Employees").child(userID);
        employeeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Use getValue(Long.class) for numeric data
                    Long employeeIdLong = snapshot.child("EmployeeID").getValue(Long.class);

                    // Check if employeeId is not null before converting to String
                    if (employeeIdLong != null) {
                        // Convert the Long employeeId to String if needed
                        employeeId = Long.valueOf(String.valueOf(employeeIdLong));

                        // Add log statements to check if values are retrieved
                        Log.d("FirebaseData", "Employee ID: " + employeeId);

                        // Update references to TextInputEditText with TextViews
                        EditText employeeIDEditText = findViewById(R.id.employeeIDEditText);
                        employeeIDEditText.setText(String.valueOf(employeeId));

                    } else {
                        // Handle the case where the employeeId is null
                        Log.d("FirebaseData", "Employee ID is null");
                    }

                    employeeName = snapshot.child("Name").getValue(String.class);
                    // Add log statements to check if values are retrieved
                    Log.d("FirebaseData", "Employee Name: " + employeeName);
                     department = snapshot.child("Department").getValue(String.class);

                    // Update references to TextInputEditText with TextViews
                    EditText employeeNameEditText = findViewById(R.id.employeeNameEditText);
                    employeeNameEditText.setText(employeeName);
                } else {
                    // Handle the case where the employee details are not found
                    Toast.makeText(getApplicationContext(), "Employee details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDateTime = getCurrentDateTime();

                String startDate = startDateBtn.getText().toString();
                String endDate = endDateBtn.getText().toString();
                String leaveReasonText = leaveReason.getText().toString();

                // Create a unique key for the leave request
                String leaveRequestId = "id" + (new Date()).getTime();

                // Create an EmployeeLeave object
                String absenceReasonText = absenceReason.getText().toString();

                EmployeeLeave employeeLeave = new EmployeeLeave(
                        leaveRequestId,
                        employeeId,
                        employeeName,
                        startDate,
                        endDate,
                        leaveReasonText,
                        absenceReasonText,  // Updated to use absenceReasonText instead of absenceReason
                        currentDateTime,
                        userID,
                        "Pending",
                        department
                );
                // Save the EmployeeLeave object to the database
                databaseReference.child("EmployeeLeave").child(leaveRequestId).setValue(employeeLeave)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Leave request submitted successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to submit leave request", Toast.LENGTH_SHORT).show();
                            Log.e("FirebaseError", "Error writing leave request to Firebase", e);
                        }
                    });
            }
        });
    }

    private String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy, HH:mm:ss");
        return dateFormat.format(currentDate);
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);

                // Set the selected date to the corresponding button
                if (selectedButton != null) {
                    selectedButton.setText(date);

                    // Update the corresponding date variable
                    if (selectedButton.getId() == R.id.startDateButton) {
                        startDate = date;
                    } else if (selectedButton.getId() == R.id.endDateButton) {
                        endDate = date;
                    }
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        // Format day and month with leading zeros if needed
        String formattedDay = (day < 10) ? "0" + day : String.valueOf(day);
        String formattedMonth = (month < 10) ? "0" + month : String.valueOf(month);

        return formattedDay + "-" + formattedMonth + "-" + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                return "JAN";
        }
    }

    public void openDatePicker(View view) {
        // Set the selectedButton variable to the clicked button
        selectedButton = (Button) view;
        datePickerDialog.show();
    }
}
