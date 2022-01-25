package com.example.onlinevoting.admin.candidates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevoting.R;
import com.example.onlinevoting.admin.AdminHomeActivity;
import com.example.onlinevoting.admin.voters.ViewVoterProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ViewCandidateActivity extends AppCompatActivity {

    ImageView profileImage;
    ImageView partyImage;
    ImageView aadhaarFrontImage;
    ImageView aadhaarBackImage;

    private TextView fullName;
    private TextView partyName;
//    private TextView mobile;
//    private TextView voterID;

    private Button btnApprove;
    private Button delete;

    private String isApproved;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_candidate);

        fullName = findViewById(R.id.c_name);
        partyName = findViewById(R.id.p_name);

        profileImage = findViewById(R.id.c_profile_image);
        partyImage = findViewById(R.id.p_Image);
        aadhaarFrontImage = findViewById(R.id.c_adhaar_front_image);
        aadhaarBackImage = findViewById(R.id.c_adhaar_back_image);

        btnApprove = findViewById(R.id.candidate_btn_approve);
        delete = findViewById(R.id.candidate_btn_delete);

        Picasso.get().load(getIntent().getStringExtra("profileImage")).placeholder(R.drawable.profile).into(profileImage);
        Picasso.get().load(getIntent().getStringExtra("partyImage")).placeholder(R.drawable.profile).into(partyImage);

        fullName.setText(getIntent().getStringExtra("name"));
        partyName.setText(getIntent().getStringExtra("partyName"));

        Picasso.get().load(getIntent().getStringExtra("aadhaarFront")).placeholder(R.drawable.aadhaarfront).into(aadhaarFrontImage);
        Picasso.get().load(getIntent().getStringExtra("aadhaarBack")).placeholder(R.drawable.aadhaarback).into(aadhaarBackImage);

        id = getIntent().getStringExtra("id");
        isApproved = getIntent().getStringExtra("isApproved");

        if (isApproved.equals("Yes")) {
            btnApprove.setText("Disapprove");
        } else {
            btnApprove.setText("Approve");
        }

        btnApprove.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ViewCandidateActivity.this).create();

                if (btnApprove.getText().equals("Approve")) {
                    alertDialog.setTitle("Do you want to Approve Candidate?");
                } else {
                    alertDialog.setTitle("Do you want to Reject Candidate?");
                }

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (btnApprove.getText().equals("Approve")) {
                            FirebaseDatabase.getInstance().getReference().child("candidate").child(id).child("isApproved").setValue("Yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        btnApprove.setText("Disapprove");
                                        Toast.makeText(ViewCandidateActivity.this, "Candidate Approved.", Toast.LENGTH_SHORT).show();
                                        addNotification(id, "Your Account is Approved");
                                    }
                                }
                            });
                        } else {
                            FirebaseDatabase.getInstance().getReference().child("candidate").child(id).child("isApproved").setValue("No").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        btnApprove.setText("Approve");
                                        Toast.makeText(ViewCandidateActivity.this, "Candidate Disapproved.", Toast.LENGTH_SHORT).show();
                                        addNotification(id, "Your Request as a Candidate is Disapproved");
                                    }
                                }
                            });
                        }
                    }
                });

                alertDialog.show();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ViewCandidateActivity.this).create();

                alertDialog.setTitle("Do you want to Delete the Candidate?");

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("candidate").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ViewCandidateActivity.this, "Candidate Request Deleted.", Toast.LENGTH_SHORT).show();
                                    addNotification(id, "Your Request is Rejected");
                                    startActivity(new Intent(ViewCandidateActivity.this, AdminHomeActivity.class));
                                    finish();
                                }
                            }
                        });

                    }
                });

                alertDialog.show();
            }
        });
    }

    private void addNotification(String voteId, String msq) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("userid",voteId);
        map.put("text", msq);

        FirebaseDatabase.getInstance().getReference().child("Notification").child(voteId).push().setValue(map);
    }
}