package com.example.onlinevoting.users.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevoting.R;
import com.example.onlinevoting.StartActivity;
import com.example.onlinevoting.models.votersModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    private TextView fullName;
    private TextView isApproved;
    private TextView isProfileComplete;
    private Button editProfile;

    String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileId = FirebaseAuth.getInstance().getUid();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(profileId).child("profile");

        ImageView logOut = view.findViewById(R.id.options);
        ImageView profileImage = view.findViewById(R.id.image_profile);
        fullName = view.findViewById(R.id.full_name);
        isProfileComplete = view.findViewById(R.id.is_profile_complete);
        isApproved = view.findViewById(R.id.is_approved);
        editProfile = view.findViewById(R.id.edit_profile);

        editProfile.setEnabled(false);

        try {
            final File localFile = File.createTempFile("profile", "jpeg");

            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profileImage.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        userInfo();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        return view;
    }

    private void userInfo() {

        FirebaseDatabase.getInstance().getReference().child("voters").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                votersModel model = snapshot.getValue(votersModel.class);

                assert model != null;
                fullName.setText(model.getName());
                isProfileComplete.setText(model.getIsProfileComplete());
                if (model.getIsProfileComplete().equals("Yes")) {
                    isProfileComplete.setTextColor(Color.parseColor("#00FF00"));
                } else {
                    isProfileComplete.setTextColor(Color.parseColor("#FF4500"));
                }
                isApproved.setText(model.getIsVerified());
                if (model.getIsVerified().equals("Yes")) {
                    isApproved.setTextColor(Color.parseColor("#00FF00"));
                } else {
                    editProfile.setEnabled(true);
                    isApproved.setTextColor(Color.parseColor("#FF4500"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}