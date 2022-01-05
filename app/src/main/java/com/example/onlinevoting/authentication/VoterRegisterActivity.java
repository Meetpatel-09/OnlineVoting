package com.example.onlinevoting.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.onlinevoting.R;

public class VoterRegisterActivity extends AppCompatActivity {

    private EditText fName, email, pwd, voterID, num;
    private String sName, sEmail, sPwd, sVoterID, sNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_register);

        fName = findViewById(R.id.reg_name);
        email = findViewById(R.id.reg_email);
        pwd = findViewById(R.id.reg_password);
        voterID = findViewById(R.id.reg_enroll);
        num = findViewById(R.id.reg_phone);
        Button btnReg = findViewById(R.id.btn_reg);
        TextView openLogIn = findViewById(R.id.tv_login);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        openLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoterRegisterActivity.this, VoterLogInActivity.class));
                finish();
            }
        });
    }

    private void validateData() {
        sName = fName.getText().toString();
        sEmail = email.getText().toString();
        sPwd = pwd.getText().toString();
        sVoterID = voterID.getText().toString();
        sNum = num.getText().toString();

        if (sName.isEmpty()) {
            fName.setError("Required");
            fName.requestFocus();
        } else if (sEmail.isEmpty()) {
            email.setError("Required");
            email.requestFocus();
        } else if (sPwd.isEmpty()) {
            pwd.setError("Required");
            pwd.requestFocus();
        } else if (sVoterID.isEmpty()) {
            voterID.setError("Required");
            voterID.requestFocus();
        } else if (sNum.isEmpty()) {
            num.setError("Required");
            num.requestFocus();
        } else {
//            createUser();
        }
    }
}