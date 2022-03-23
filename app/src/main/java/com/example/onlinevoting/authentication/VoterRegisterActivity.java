package com.example.onlinevoting.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevoting.users.MainActivity;
import com.example.onlinevoting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class VoterRegisterActivity extends AppCompatActivity {

    private EditText fName, email, pwd, cPwd, num;
    private String sName, sEmail, sPwd, sCPwd, sNum;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_register);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        pd = new ProgressDialog(this);

        fName = findViewById(R.id.reg_name);
        email = findViewById(R.id.reg_email);
        pwd = findViewById(R.id.reg_password);
        cPwd = findViewById(R.id.reg_c_password);
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

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            openMain();
        }
    }

    private void openMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void openCProfile() {
        startActivity(new Intent(this, VoterCompleteProfileActivity.class));
        finish();
    }

    private void validateData() {
        sName = fName.getText().toString();
        sEmail = email.getText().toString();
        sPwd = pwd.getText().toString();
        sCPwd = cPwd.getText().toString();
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
        } else if (sCPwd.isEmpty()) {
            pwd.setError("Required");
            pwd.requestFocus();
        } else if (sNum.isEmpty()) {
            num.setError("Required");
            num.requestFocus();
        } else if (sNum.length() != 10) {
            num.setError("Invalid");
            num.requestFocus();
        } else {
            if (!sPwd.equals(sCPwd)) {
                cPwd.setError("Password not match");
            } else {
                createUser();
            }
        }
    }

    private void createUser() {
        pd.setMessage("Loading...");
        pd.show();
        auth.createUserWithEmailAndPassword(sEmail, sPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    uploadData();
                } else {
                    pd.dismiss();
                    Toast.makeText(VoterRegisterActivity.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(VoterRegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData() {

        HashMap<String, String> map = new HashMap<>();
        map.put("id", Objects.requireNonNull(auth.getCurrentUser()).getUid());
        map.put("name", sName);
        map.put("email", sEmail);
        map.put("isProfileComplete", "No");
        map.put("isVerified", "No");
        map.put("phone", sNum);
        map.put("password", sPwd);

        reference.child("voters").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(VoterRegisterActivity.this, "Registration Successful!!!", Toast.LENGTH_SHORT).show();
                    openCProfile();
                } else {
                    pd.dismiss();
                    Toast.makeText(VoterRegisterActivity.this, "Error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(VoterRegisterActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}