package com.example.onlinevoting.users.ui.election;

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

import com.example.onlinevoting.R;
import com.example.onlinevoting.models.VotersModel;
import com.example.onlinevoting.users.MainActivity;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Objects;

public class ApplyCandidateActivity extends AppCompatActivity {

    private ImageView addPartyIcon;
    private ImageView addAdhaarFrontImage;
    private ImageView getAddAdhaarBackImage;
    private ImageView adhaarFront;
    private ImageView adhaarBack;

    private TextView addPartyTxt;
    private TextView addAdhaarFrontTxt;
    private TextView getAddAdhaarBackTxt;

    private EditText partyName;

    private Button btnSubmit;

    private String sPartyName;
    private String sImage;

    private Uri partyLogoUri;
    private Uri adhaarFrontUri;
    private Uri adhaarBackUri;

    private String partyLogoUrl = "NotUploaded";
    private String adhaarFrontUrl = "NotUploaded";
    private String adhaarBackUrl = "NotUploaded";

    private FirebaseUser fUser;
    private FirebaseStorage storage;
    private DatabaseReference reference;

    private StorageTask uploadTask;

    private String name, email, number, voterID, profileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_candidate);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        addPartyIcon = findViewById(R.id.party_logo);
        addAdhaarFrontImage = findViewById(R.id.add_adhaar_front_img);
        getAddAdhaarBackImage = findViewById(R.id.add_adhaar_back_img);
        adhaarFront = findViewById(R.id.adhaar_front_image);
        adhaarBack = findViewById(R.id.adhaar_back_image);
        addPartyTxt = findViewById(R.id.add_party_logo_text);
        addAdhaarFrontTxt = findViewById(R.id.add_adhaar_front_txt);
        getAddAdhaarBackTxt = findViewById(R.id.add_adhaar_back_txt);
        partyName = findViewById(R.id.et_party_name);
        btnSubmit = findViewById(R.id.add_party_btn);

        addPartyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sImage = "group";
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(ApplyCandidateActivity.this);
            }
        });

        addPartyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sImage = "group";
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(ApplyCandidateActivity.this);
            }
        });

        addAdhaarFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sImage = "front";
                CropImage.activity().start(ApplyCandidateActivity.this);
            }
        });

        addAdhaarFrontTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sImage = "front";
                CropImage.activity().start(ApplyCandidateActivity.this);
            }
        });

        getAddAdhaarBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sImage = "back";
                CropImage.activity().start(ApplyCandidateActivity.this);
            }
        });

        getAddAdhaarBackTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sImage = "back";
                CropImage.activity().start(ApplyCandidateActivity.this);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            assert result != null;
            if (sImage.equals("group")) {
                partyLogoUri = result.getUri();
                addPartyIcon.setImageURI(result.getUri());
                uploadImage("partyLogo.jpeg", partyLogoUri, "partyLogo");
            } else if (sImage.equals("front")){
                adhaarFrontUri = result.getUri();
                adhaarFront.setImageURI(result.getUri());
                uploadImage("adhaarFront.jpeg", adhaarFrontUri, "adhaarFront");
            } else {
                adhaarBackUri = result.getUri();
                adhaarBack.setImageURI(result.getUri());
                uploadImage("adhaarBack.jpeg", adhaarBackUri, "adhaarBack");
            }
        } else {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(String imageName, Uri ImageUri, String imageNameInDB) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        final StorageReference referenceForProfile = storage.getReference().child("candidates")
                .child(fUser.getUid()).child(imageName);

        uploadTask = referenceForProfile.putFile(ImageUri);
        uploadTask.addOnCompleteListener(ApplyCandidateActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            referenceForProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (sImage.equals("group")) {
                                        partyLogoUrl = String.valueOf(uri);
                                    } else if (sImage.equals("front")){
                                        adhaarFrontUrl = String.valueOf(uri);
                                    } else {
                                        adhaarBackUrl = String.valueOf(uri);
                                    }
                                    pd.dismiss();
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(ApplyCandidateActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkData() {
        sPartyName = partyName.getText().toString();

        if (partyLogoUrl.equals("NotUploaded")) {
            Toast.makeText(ApplyCandidateActivity.this, "Please Upload Party Logo Image", Toast.LENGTH_SHORT).show();
        } else if (adhaarFrontUrl.equals("NotUploaded")) {
            Toast.makeText(ApplyCandidateActivity.this, "Please Upload Aadhaar Front Image", Toast.LENGTH_SHORT).show();
        } else if (adhaarBackUrl.equals("NotUploaded")) {
            Toast.makeText(ApplyCandidateActivity.this, "Please Upload Aadhaar Back Images", Toast.LENGTH_SHORT).show();
        } else if (sPartyName.isEmpty()) {
            partyName.setError("Please Enter Party Name");
        } else {
            getVoterData();
        }
    }

    private void getVoterData() {
        FirebaseDatabase.getInstance().getReference().child("voters").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                VotersModel model = snapshot.getValue(VotersModel.class);
                name = model.getName();
                email = model.getEmail();
                number = model.getPhone();
                voterID = model.getVoterID();
                profileUrl = model.getProfileImage();
                uploadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadData() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        HashMap<String, String> map = new HashMap<>();
        map.put("id", fUser.getUid());
        map.put("name", name);
        map.put("email", email);
        map.put("phone", number);
        map.put("partyName", sPartyName);
        map.put("profileImage", profileUrl);
        map.put("partyImage", partyLogoUrl);
        map.put("aadhaarFront", adhaarFrontUrl);
        map.put("aadhaarBack", adhaarBackUrl);
        map.put("isApproved", "No");

        reference.child("candidate").child(fUser.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(ApplyCandidateActivity.this, "Applied Successful!!!", Toast.LENGTH_SHORT).show();
                    openHome();
                } else {
                    pd.dismiss();
                    Toast.makeText(ApplyCandidateActivity.this, "Error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(ApplyCandidateActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}