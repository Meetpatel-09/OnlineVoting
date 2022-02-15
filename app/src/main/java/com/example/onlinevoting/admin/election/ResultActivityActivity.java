package com.example.onlinevoting.admin.election;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinevoting.R;
import com.example.onlinevoting.models.ElectionModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ResultActivityActivity extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    private EditText editText;
    private Button btnViewElection;
    String electionDate;

    private DatabaseReference refElection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_activity);

        editText = findViewById(R.id.view_election_date);
        btnViewElection = findViewById(R.id.btn_view_election);

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
                new DatePickerDialog(ResultActivityActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnViewElection.setOnClickListener(new View.OnClickListener() {
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

                }
                else  {
                    Toast.makeText(ResultActivityActivity.this, "No Election On " + electionDate, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}