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
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.StorageTask;
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

    private String profileImageUrl;
    private String voterIDImageUrl;

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

    private StorageTask uploadTask;

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
                uploadProfileImage("profile.jpeg", profileImageUri, true, "profileImage");
            } else {
                isVoterIDUpdated = true;
                voterIDImageUri = result.getUri();
                voterIDImage.setImageURI(result.getUri());
                uploadProfileImage("voterID.jpeg", voterIDImageUri, false, "voterIDImage");
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("voters");

        reference.child(fUser.getUid()).child("isProfileComplete").setValue("Yes");
        reference.child(fUser.getUid()).child("voterID").setValue(sVoterID);
        pd.dismiss();
        startActivity(new Intent(VoterCompleteProfileActivity.this, MainActivity.class));
        finish();
        Toast.makeText(
                VoterCompleteProfileActivity.this, "Uploaded Successfully.", Toast.LENGTH_SHORT).show();

    }

    private void uploadProfileImage(String imageName, Uri ImageUri, boolean isProfile, String imageNameInDB) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        final StorageReference referenceForProfile = storage.getReference().child("voters")
                .child(fUser.getUid()).child(imageName);

        uploadTask = referenceForProfile.putFile(ImageUri);
        uploadTask.addOnCompleteListener(VoterCompleteProfileActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            referenceForProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (isProfile) {
                                        profileImageUrl = String.valueOf(uri);
                                        updateDatabase(imageNameInDB, profileImageUrl);
                                    } else {
                                        voterIDImageUrl = String.valueOf(uri);
                                        updateDatabase(imageNameInDB, voterIDImageUrl);
                                    }
                                    pd.dismiss();
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(VoterCompleteProfileActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(VoterCompleteProfileActivity.this, VoterCompleteProfileActivity.class));
                }
            }
        });
    }

    private void updateDatabase(String imagefor, String imageURL) {
        FirebaseDatabase.getInstance().getReference().child("voters").child(fUser.getUid()).child(imagefor).setValue(imageURL);
    }
}