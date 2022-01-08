package com.example.onlinevoting.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevoting.users.MainActivity;
import com.example.onlinevoting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class VoterCompleteProfileActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private ImageView voterIDImage;
    private TextView addProfileImageText;
    private TextView addVoterIDImageText;
    private EditText voterID;

    private Uri profileImageUri;
    private Uri voterIDImageUri;

    private boolean isProfile;
    private boolean isProfileUpdated = false;
    private boolean isVoterIDUpdated = false;
    private boolean isProfileUpLoaded = false;
    private boolean isVoterIDUpLoaded = false;

    private String sVoterID;

    private Button submit;

    private ProgressDialog pd;

    private FirebaseUser fUser;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_complete_profile);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();

        pd = new ProgressDialog(this);

        profileImage = findViewById(R.id.profile_image);
        voterIDImage = findViewById(R.id.voter_id_card_image);
        addProfileImageText = findViewById(R.id.add_profile_image_text);
        addVoterIDImageText = findViewById(R.id.add_voter_id_card_text);
        voterID = findViewById(R.id.et_voter_id);

        submit = findViewById(R.id.submit_button);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProfile = true;
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(VoterCompleteProfileActivity.this);
            }
        });

        addProfileImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProfile = true;
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(VoterCompleteProfileActivity.this);
            }
        });

        voterIDImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProfile = false;
                CropImage.activity().start(VoterCompleteProfileActivity.this);
            }
        });

        addVoterIDImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProfile = false;
                CropImage.activity().start(VoterCompleteProfileActivity.this);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            assert result != null;
            if (isProfile) {
                isProfileUpdated = true;
                profileImageUri = result.getUri();
                profileImage.setImageURI(result.getUri());
            } else {
                isVoterIDUpdated = true;
                voterIDImageUri = result.getUri();
                voterIDImage.setImageURI(result.getUri());
            }
        } else {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private void validate() {
        sVoterID = voterID.getText().toString();

        if (sVoterID.isEmpty()) {
            voterID.setError("Please enter Voter ID");
            voterID.requestFocus();
        } else if (!isProfileUpdated) {
            Toast.makeText(VoterCompleteProfileActivity.this, "Please Upload Profile Picture", Toast.LENGTH_SHORT).show();
        } else if (!isVoterIDUpdated) {
            Toast.makeText(VoterCompleteProfileActivity.this, "Please Upload Voter ID Card Picture", Toast.LENGTH_SHORT).show();
        } else {
            updateProfile();
        }
    }

    private void updateProfile() {
        pd.setMessage("Uploading...");
        pd.show();
        final StorageReference referenceForProfile = storage.getReference()
                .child(fUser.getUid()).child("profile");

        final StorageReference referenceForVoterID = storage.getReference()
                .child(fUser.getUid()).child("voterID");

        referenceForProfile.putFile(profileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                isProfileUpLoaded = true;
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                referenceForVoterID.putFile(voterIDImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        isVoterIDUpLoaded = true;
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("voters");

                        reference.child(fUser.getUid()).child("isProfileComplete").setValue("Yes");
                        reference.child(fUser.getUid()).child("voterID").setValue(sVoterID);
                        pd.dismiss();
                        startActivity(new Intent(VoterCompleteProfileActivity.this, MainActivity.class));
                        finish();
                        Toast.makeText(
                                VoterCompleteProfileActivity.this, "Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(VoterCompleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(VoterCompleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}