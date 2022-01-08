package com.example.onlinevoting.authentication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevoting.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class VoterCompleteProfileActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private ImageView voterIDImage;
    private TextView addProfileImageText;
    private TextView addVoterIDImageText;
    private EditText voterID;

    private Uri mImaeUri;
    private boolean isProfile;

    private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_complete_profile);

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
                checkVoterID();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (isProfile) {
                profileImage.setImageURI(result.getUri());
            } else {
                voterIDImage.setImageURI(result.getUri());
            }
//            uploadImage();
        } else {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkVoterID() {
        String sVoterID = voterID.getText().toString();

        if (sVoterID.isEmpty()) {
            voterID.setError("Please enter Voter ID");
            voterID.requestFocus();
        } else {
            updateProfile();
        }
    }

    private void updateProfile() {
    }
}