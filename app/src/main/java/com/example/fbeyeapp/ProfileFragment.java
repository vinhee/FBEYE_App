package com.example.fbeyeapp;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference reference;

    StorageReference ImgReference;
    FirebaseAuth mAuth;

    FirebaseUser currentUser;
    CircleImageView uploadImage;
    StorageReference storageProfilePicsRef;

    TextView staffIDTextView;
    TextView staffNameTextView;
    TextView staffEmailTextView;

    Uri imagePath;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        uploadImage = view.findViewById(R.id.uploadImage);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("ProfilePicture/*");
                startActivityForResult(photoIntent, 1);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Employees");
        currentUser = mAuth.getCurrentUser();
        String targetID = currentUser.getUid();
        Log.d(TAG,"Current User: " + targetID);
        ImgReference = FirebaseStorage.getInstance().getReference("ProfilePicture/" +targetID+".png");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference();

        staffIDTextView = view.findViewById(R.id.StaffID);
        staffNameTextView = view.findViewById(R.id.StaffName);
        staffEmailTextView = view.findViewById(R.id.StaffEmail);

        if (currentUser != null) {
            String targetEmail = currentUser.getEmail();
            ImgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageUrl = uri.toString();
                    // Load the image into ImageView using Glide
                    Glide.with(requireContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.profile_icon)
                            .into(uploadImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });


            reference.orderByChild("Email").equalTo(targetEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String staffIDDB = String.valueOf(userSnapshot.child("EmployeeID").getValue(long.class));
                            String staffEmailDB = String.valueOf(userSnapshot.child("Email").getValue(String.class));
                            String staffNameDB = String.valueOf(userSnapshot.child("Name").getValue(String.class));
                            staffNameTextView.setText(staffNameDB);
                            staffIDTextView.setText(staffIDDB);
                            staffEmailTextView.setText(staffEmailDB);

                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imagePath = data.getData();
            getImageInImageView();
            uploadImage(); // Call uploadImage method here
        }
    }

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        uploadImage.setImageBitmap(bitmap);
    }


    private void uploadImage() {
        if (imagePath == null) {
            // Handle the case where the user didn't choose an image to upload
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading.....");
        progressDialog.show();

        FirebaseStorage.getInstance().getReference("images/" + UUID.randomUUID().toString()).putFile(imagePath)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Get the download URL of the uploaded image
                            task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    progressDialog.dismiss();
                                    String imageUrl = downloadUri.toString();
                                    // Now you can save the download URL to your Firebase Realtime Database
                                    DatabaseReference userRef = reference.child(currentUser.getUid());
                                    userRef.child("profileImageURL").setValue(imageUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getContext(), "Profile image uploaded successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Failed to upload profile image.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed to upload image.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
