package com.example.onlinevoting.admin.election;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinevoting.R;
import com.example.onlinevoting.admin.AdminHomeActivity;
import com.example.onlinevoting.models.ElectionModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SetElectionActivity extends AppCompatActivity{

    final Calendar myCalendar= Calendar.getInstance();
    private EditText editText;
    private Button btnSetElection;
    String electionDate;

    private DatabaseReference reference, dbRef;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_election);

        editText = findViewById(R.id.election_date);
        btnSetElection = findViewById(R.id.btn_set_election);

        reference = FirebaseDatabase.getInstance().getReference();

        editText.setHint("Election Date");
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SetElectionActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSetElection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

    }

    private void updateLabel(){
        String myFormat="dd-MM-yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        editText.setHint("");
        electionDate = dateFormat.format(myCalendar.getTime());
        editText.setText(electionDate);
    }

    private void uploadData() {
        dbRef = reference.child("Election");
        final String uniqueKey = dbRef.push().getKey();

        ElectionModel model = new ElectionModel(electionDate, uniqueKey);
        dbRef.child(electionDate).child("electionData").setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SetElectionActivity.this, "Election Added Successfully", Toast.LENGTH_SHORT).show();
                openDash();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(SetElectionActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                openDash();
            }
        });
    }

    private void openDash() {
        startActivity(new Intent(SetElectionActivity.this, AdminHomeActivity.class));
        finish();
    }
}