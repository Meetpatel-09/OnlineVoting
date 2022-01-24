package com.example.onlinevoting.admin.voters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.onlinevoting.R;
import com.example.onlinevoting.models.NoticeModel;
import com.example.onlinevoting.models.VotersModel;
import com.example.onlinevoting.users.ui.notice.NoticeAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerifyVotersActivity extends AppCompatActivity {
    private RecyclerView votersRecyclerview;
    private ProgressBar progressBar;
    private ArrayList<VotersModel> list;
    private VotersAdapter adapter;

    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_voters);

        progressBar = findViewById(R.id.progress_bar_v);

        votersRecyclerview = findViewById(R.id.recycler_view_voters);
        votersRecyclerview.setHasFixedSize(true);
        votersRecyclerview.setLayoutManager(new LinearLayoutManager(VerifyVotersActivity.this));
        list = new ArrayList<>();
        adapter = new VotersAdapter(VerifyVotersActivity.this, list);
        votersRecyclerview.setAdapter(adapter);

        voters();
    }

    private void voters() {
        FirebaseDatabase.getInstance().getReference().child("voters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot :snapshot.getChildren()) {
                    VotersModel model = dataSnapshot.getValue(VotersModel.class);
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