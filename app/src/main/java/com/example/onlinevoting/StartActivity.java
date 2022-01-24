package com.example.onlinevoting;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.onlinevoting.authentication.AdminLogInActivity;
import com.example.onlinevoting.authentication.VoterLogInActivity;
import com.example.onlinevoting.authentication.VoterRegisterActivity;
import com.example.onlinevoting.users.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btnVoterReg = findViewById(R.id.btn_voter_reg);
        Button btnVoterLogIn = findViewById(R.id.btn_voter_login);
        Button btnAdminLogIn = findViewById(R.id.btn_admin_login);

        btnVoterReg.setOnClickListener(this);
        btnVoterLogIn.setOnClickListener(this);
        btnAdminLogIn.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_voter_reg:
                startActivity(new Intent(this, VoterRegisterActivity.class));
                break;

            case R.id.btn_voter_login:
                startActivity(new Intent(this, VoterLogInActivity.class));
                break;

            case R.id.btn_admin_login:
                startActivity(new Intent(this, AdminLogInActivity.class));
                break;

        }
    }
}