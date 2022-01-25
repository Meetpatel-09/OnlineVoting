package com.example.onlinevoting.admin.voters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevoting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ViewVoterProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    ImageView voterImage;

    private TextView fullName;
//    private TextView isApproved;
//    private TextView isProfileComplete;
    private TextView email;
    private TextView mobile;
    private TextView voterID;

    private Button btnApprove;

    private String isApproved;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_voter_profile);

        profileImage = findViewById(R.id.v_image_profile);
        voterImage = findViewById(R.id.v_voter_id_card_image);
        fullName = findViewById(R.id.v_full_name);
//        isProfileComplete = findViewById(R.id.v_is_profile_complete);
//        isApproved = findViewById(R.id.v_is_approved);
        email = findViewById(R.id.txt_email);
        mobile = findViewById(R.id.txt_mobile_num);
        voterID = findViewById(R.id.txt_voter_id);
        btnApprove = findViewById(R.id.p_btn_approve);

        Picasso.get().load(getIntent().getStringExtra("profileImage")).placeholder(R.drawable.profile).into(profileImage);
        Picasso.get().load(getIntent().getStringExtra("voterIDImage")).placeholder(R.drawable.voter_id).into(voterImage);
        fullName.setText(getIntent().getStringExtra("name"));
        email.setText(getIntent().getStringExtra("email"));
        mobile.setText(getIntent().getStringExtra("mobile"));
//        voterID.setText(getIntent().getStringExtra("voterID"));
        voterID.setText(getIntent().getStringExtra("isApproved"));

        isApproved = getIntent().getStringExtra("isApproved");
        id = getIntent().getStringExtra("id");

        if (isApproved.equals("Yes")) {
            btnApprove.setText("Disapprove");
        } else {
            btnApprove.setText("Approve");
        }

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ViewVoterProfileActivity.this).create();

                if (btnApprove.getText().equals("Approve")) {
                    alertDialog.setTitle("Do you want to Approve Voter?");
                } else {
                    alertDialog.setTitle("Do you want to Reject Voter?");
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
                            FirebaseDatabase.getInstance().getReference().child("voters").child(id).child("isVerified").setValue("Yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        btnApprove.setText("Disapprove");
                                        Toast.makeText(ViewVoterProfileActivity.this, "Voter Approved.", Toast.LENGTH_SHORT).show();
                                        addNotification(id, "Your Account is Approved");
                                    }
                                }
                            });
                        } else {
                            FirebaseDatabase.getInstance().getReference().child("voters").child(id).child("isVerified").setValue("No").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        btnApprove.setText("Approve");
                                        Toast.makeText(ViewVoterProfileActivity.this, "Voter Disapproved.", Toast.LENGTH_SHORT).show();
                                        addNotification(id, "Your Account is Disapproved");
                                    }
                                }
                            });
                        }
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