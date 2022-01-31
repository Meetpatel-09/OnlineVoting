package com.example.onlinevoting.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.onlinevoting.R;
import com.example.onlinevoting.StartActivity;
import com.example.onlinevoting.admin.candidates.ApproveCandidateActivity;
import com.example.onlinevoting.admin.election.DeleteElectionActivity;
import com.example.onlinevoting.admin.election.SetElectionActivity;
import com.example.onlinevoting.admin.election.ViewResultsActivity;
import com.example.onlinevoting.admin.notice.DeleteNoticeActivity;
import com.example.onlinevoting.admin.notice.UploadNoticeActivity;
import com.example.onlinevoting.admin.voters.VerifyVotersActivity;
import com.example.onlinevoting.authentication.AdminLogInActivity;
import com.google.android.material.card.MaterialCardView;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialCardView uploadNotice, deleteNotice, verifyVoters, approveCandidates, setElection, deleteElection, viewResults;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getString("isLogin", "false").equals("false")) {
            startActivity(new Intent(AdminHomeActivity.this, AdminLogInActivity.class));
            finish();
        }

        uploadNotice = findViewById(R.id.add_notification);
        deleteNotice = findViewById(R.id.delete_notice);
        verifyVoters = findViewById(R.id.verify_voters);
        approveCandidates = findViewById(R.id.approve_candidate);
        setElection = findViewById(R.id.set_election);
        deleteElection = findViewById(R.id.delete_election);
        viewResults = findViewById(R.id.view_results);
        logOut = findViewById(R.id.btn_logout);

        uploadNotice.setOnClickListener(this);
        deleteNotice.setOnClickListener(this);
        verifyVoters.setOnClickListener(this);
        approveCandidates.setOnClickListener(this);
        setElection.setOnClickListener(this);
        deleteElection.setOnClickListener(this);
        viewResults.setOnClickListener(this);
        logOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.add_notification:
                intent = new Intent(AdminHomeActivity.this, UploadNoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.delete_notice:
                intent = new Intent(AdminHomeActivity.this, DeleteNoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.verify_voters:
                intent = new Intent(AdminHomeActivity.this, VerifyVotersActivity.class);
                startActivity(intent);
                break;
            case R.id.approve_candidate:
                intent = new Intent(AdminHomeActivity.this, ApproveCandidateActivity.class);
                startActivity(intent);
                break;
            case R.id.set_election:
                intent = new Intent(AdminHomeActivity.this, SetElectionActivity.class);
                startActivity(intent);
                break;
            case R.id.delete_election:
                intent = new Intent(AdminHomeActivity.this, DeleteElectionActivity.class);
                startActivity(intent);
                break;
            case R.id.view_results:
                intent = new Intent(AdminHomeActivity.this, ViewResultsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_logout:
                editor.putString("isLogin", "false");
                editor.commit();
                intent = new Intent(AdminHomeActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

}