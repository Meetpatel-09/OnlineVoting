package com.example.onlinevoting.admin.notice;

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
import android.widget.Toast;

import com.example.onlinevoting.R;
import com.example.onlinevoting.admin.AdminHomeActivity;
import com.example.onlinevoting.models.NoticeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadNoticeActivity extends AppCompatActivity {

    private MaterialCardView addImage;
    private ImageView noticeImageView;

    private EditText noticeTitle;
    private Button uploadNotice;


    private DatabaseReference reference, dbRef;

    private FirebaseStorage storage;

    private StorageTask uploadTask;

    private boolean isImageUploaded = false;

    private Uri imageUri;
    String downloadUrl = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);

        reference = FirebaseDatabase.getInstance().getReference();

        storage = FirebaseStorage.getInstance();

        pd = new ProgressDialog(this);

        addImage = findViewById(R.id.add_image);
        noticeImageView = findViewById(R.id.notice_image);
        noticeTitle = findViewById(R.id.notice_title);
        uploadNotice = findViewById(R.id.upload_notice_button);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(UploadNoticeActivity.this);
            }
        });

        uploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noticeTitle.getText().toString().isEmpty()){
                    noticeTitle.setError("Empty");
                    noticeTitle.requestFocus();
                } else if (isImageUploaded) {
                    uploadData();
                } else {
                    if (downloadUrl.equals(""))
                    {
                        Toast.makeText(UploadNoticeActivity.this, "Please upload image", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadImage();
                    }
                }
            }

        });
    }

    private void uploadData() {
        dbRef = reference.child("Notice");
        final String uniqueKey = dbRef.push().getKey();

        String title = noticeTitle.getText().toString();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        NoticeModel model = new NoticeModel(title, downloadUrl, date, time, uniqueKey);
        dbRef.child(uniqueKey).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(UploadNoticeActivity.this, "Notice Uploaded Successfully", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(UploadNoticeActivity.this, AdminHomeActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadNoticeActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UploadNoticeActivity.this, AdminHomeActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            noticeImageView.setImageURI(imageUri);
            uploadImage();
        } else {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading Image");
        pd.show();

        final StorageReference referenceForProfile = storage.getReference().child("notice").child(System.currentTimeMillis() + ".jpeg");

        uploadTask = referenceForProfile.putFile(imageUri);
        uploadTask.addOnCompleteListener(UploadNoticeActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            referenceForProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    pd.dismiss();
                                    isImageUploaded = true;
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(UploadNoticeActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}