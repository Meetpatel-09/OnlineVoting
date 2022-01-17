package com.example.onlinevoting.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevoting.R;
import com.example.onlinevoting.admin.AdminHomeActivity;

public class AdminLogInActivity extends AppCompatActivity {

    private EditText etEmail, etPwd;
    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);

        etEmail = findViewById(R.id.email);
        etPwd = findViewById(R.id.password);

        show = findViewById(R.id.tv_show);

        RelativeLayout login = findViewById(R.id.login_btn);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPwd.getInputType() == 144) {
                    etPwd.setInputType(129);
                    show.setText(R.string.hide);
                } else {
                    etPwd.setInputType(144);
                    show.setText(R.string.show);
                }
                etPwd.setSelection(etPwd.getText().length());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        String email = etEmail.getText().toString();
        String pwd = etPwd.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Please enter email");
            etEmail.requestFocus();
        } else if (pwd.isEmpty()) {
            etPwd.setError("Please enter email");
            etPwd.requestFocus();
        } else if (email.equals("admin@gmail.com") && pwd.equals("123456")) {
            openDash();
        } else {
            Toast.makeText(this, "Please enter correct email and password.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openDash() {
        startActivity(new Intent(AdminLogInActivity.this, AdminHomeActivity.class));
        finish();
    }
}