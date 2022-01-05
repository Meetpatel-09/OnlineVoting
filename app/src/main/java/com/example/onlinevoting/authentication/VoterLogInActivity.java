package com.example.onlinevoting.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.onlinevoting.R;

public class VoterLogInActivity extends AppCompatActivity {

    private EditText email, pwd;
    private TextView openReg;
    private Button btnLogin;

    private String sEmail, sPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_log_in);

        email = findViewById(R.id.login_email);
        pwd = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.btn_login);
        openReg = findViewById(R.id.tv_reg);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        openReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoterLogInActivity.this, VoterRegisterActivity.class));
                finish();
            }
        });
    }

    private void validateData() {
        sEmail = email.getText().toString();
        sPwd = pwd.getText().toString();

        if (sEmail.isEmpty()) {
            email.setError("Required");
            email.requestFocus();
        } else if (sPwd.isEmpty()) {
            pwd.setError("Required");
            pwd.requestFocus();
        } else {
//            loginUSer();
        }
    }
}