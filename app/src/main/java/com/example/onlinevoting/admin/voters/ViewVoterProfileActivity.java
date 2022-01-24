package com.example.onlinevoting.admin.voters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlinevoting.R;
import com.squareup.picasso.Picasso;

public class ViewVoterProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    ImageView voterImage;

    private TextView fullName;
//    private TextView isApproved;
//    private TextView isProfileComplete;
    private TextView email;
    private TextView mobile;
    private TextView voterID;

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

        Picasso.get().load(getIntent().getStringExtra("profileImage")).into(profileImage);
        Picasso.get().load(getIntent().getStringExtra("voterIDImage")).into(voterImage);
        fullName.setText(getIntent().getStringExtra("name"));
        email.setText(getIntent().getStringExtra("email"));
        mobile.setText(getIntent().getStringExtra("mobile"));
        voterID.setText(getIntent().getStringExtra("voterID"));
    }
}