package com.example.fbeyeapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class applyLeave extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    Button startDateBtn;
    Button endDateBtn;
    TextView absenceReason;

    Button submitBtn;
    EditText mcNO;
    EditText clinicName;
    Button uploadImageBtn;
    // Separate variables for start and end dates
    String startDate;
    String endDate;
    Long employeeId;
    String employeeName;
    String department;
    String mcNoText;
    String clinicNameText;
    Uri selectedImageUri; // Uri to store the selected image
    // Variable to store the currently selected button
    private Button selectedButton;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    ImageView backBtn;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        startDateBtn = findViewById(R.id.startDateButton);
        endDateBtn = findViewById(R.id.endDateButton);
        absenceReason = findViewById(R.id.AbsenceReason);
        mcNO = findViewById(R.id.mcNo);
        clinicName = findViewById(R.id.clinicName);
        backBtn = findViewById(R.id.backBtn);
        submitBtn = findViewById(R.id.submitButton);
        uploadImageBtn = findViewById(R.id.uploadImage);

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
                    // Add log statements to check if values are retrieved
                    Log.d("FirebaseData", "Department: " + department);

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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(applyLeave.this, MainActivity.class);
                intent.putExtra("fragmentToLoad", "HomeFragment"); // Pass the fragment to load
                startActivity(intent);

                // Finish the current activity
                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDateTime = getCurrentDateTime();

                startDate = startDateBtn.getText().toString();
                endDate = endDateBtn.getText().toString();
                mcNoText = mcNO.getText().toString();
                clinicNameText = clinicName.getText().toString();

                // Create a unique key for the leave request
                String leaveRequestId = "id" + (new Date()).getTime();

                // Create an EmployeeLeave object
                String absenceReasonText = absenceReason.getText().toString();

                EmployeeLeaveMC employeeLeaveMC = new EmployeeLeaveMC(
                        leaveRequestId,
                        employeeId,
                        employeeName,
                        startDate,
                        endDate,
                        absenceReasonText,
                        currentDateTime,
                        userID,
                        "Pending",
                        department,
                        mcNoText,
                        clinicNameText
                );


                // Save the EmployeeLeave object to the database
                databaseReference.child("EmployeeLeave").child(leaveRequestId).setValue(employeeLeaveMC)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Leave request submitted successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(applyLeave.this, MainActivity.class);
                                intent.putExtra("fragmentToLoad", "HomeFragment");
                                startActivity(intent);

                                // Finish the current activity
                                finish();

                                // Upload the image if selected
                                uploadImage(leaveRequestId);
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

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No image picker app installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(String leaveRequestId) {
        if (selectedImageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("MC").child(leaveRequestId);

            storageReference.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                            // Get the download URL of the uploaded image
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    // Now you can use downloadUri to store the URL in the database or perform other actions
                                    String imageUrl = downloadUri.toString();
                                    Log.d("FirebaseData", "Image URL: " + imageUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                            Log.e("FirebaseError", "Error uploading image to Firebase Storage", e);
                        }
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
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
}
