package com.example.onlinevoting.admin.candidates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.onlinevoting.R;
import com.example.onlinevoting.admin.voters.VerifyVotersActivity;
import com.example.onlinevoting.admin.voters.VotersAdapter;
import com.example.onlinevoting.models.CandidateModel;
import com.example.onlinevoting.models.VotersModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApproveCandidateActivity extends AppCompatActivity {
    private RecyclerView candidateRecyclerview;
    private ProgressBar progressBar;
    private ArrayList<CandidateModel> list;
    private CandidateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_candidate);

        progressBar = findViewById(R.id.progress_bar_c);

        candidateRecyclerview = findViewById(R.id.candidates_recycler_view);
        candidateRecyclerview.setHasFixedSize(true);
        candidateRecyclerview.setLayoutManager(new LinearLayoutManager(ApproveCandidateActivity.this));
        list = new ArrayList<>();
        adapter = new CandidateAdapter(ApproveCandidateActivity.this, list);
        candidateRecyclerview.setAdapter(adapter);

        candidates();
    }

    private void candidates() {
        FirebaseDatabase.getInstance().getReference().child("candidate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot :snapshot.getChildren()) {
                    CandidateModel model = dataSnapshot.getValue(CandidateModel.class);
                    list.add(model);
                }
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}