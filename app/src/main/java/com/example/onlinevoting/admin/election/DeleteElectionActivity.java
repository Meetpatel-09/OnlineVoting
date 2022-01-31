package com.example.onlinevoting.admin.election;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DeleteElectionActivity extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    private EditText editText;
    private Button btnDeleteElection;
    String electionDate;

    private DatabaseReference reference, dbRef, refElection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_election);

        editText = findViewById(R.id.d_election_date);
        btnDeleteElection = findViewById(R.id.btn_delete_election);

        reference = FirebaseDatabase.getInstance().getReference();

        refElection = FirebaseDatabase.getInstance().getReference().child("Election");

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
                new DatePickerDialog(DeleteElectionActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnDeleteElection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkElection();
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

    private void checkElection() {

        refElection.child(electionDate).child("electionData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ElectionModel electionModel = snapshot.getValue(ElectionModel.class);

                if (electionModel != null) {
                    deleteData();
                }
                else  {
                    Toast.makeText(DeleteElectionActivity.this, "No Election On " + electionDate, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteData() {
        dbRef = reference.child("Election");

        dbRef.child(electionDate).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("candidate").removeValue();
                reference.child("votersVote").removeValue();
                reference.child("result").removeValue();
                openDash();
                Toast.makeText(DeleteElectionActivity.this, "Election Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(DeleteElectionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                openDash();
            }
        });
    }

    private void openDash() {
        startActivity(new Intent(DeleteElectionActivity.this, AdminHomeActivity.class));
        finish();
    }
}