package com.example.onlinevoting.admin.notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevoting.R;
import com.example.onlinevoting.models.NoticeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class DeleteNoticeActivity extends AppCompatActivity {

    private RecyclerView deleteNoticeRecyclerview;
    private ProgressBar progressBar;
    private ArrayList<NoticeModel> list;
    private NoticeAdapter adapter;

    private TextView noNotice;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);

        deleteNoticeRecyclerview = findViewById(R.id.delete_notice_recyclerview);
        progressBar = findViewById(R.id.progress_bar);

        noNotice = findViewById(R.id.a_txt_notice);

        reference = FirebaseDatabase.getInstance().getReference().child("Notice");

        deleteNoticeRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        deleteNoticeRecyclerview.setHasFixedSize(true);

        getNotice();
    }

    private void getNotice() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NoticeModel data = dataSnapshot.getValue(NoticeModel.class);
                    list.add(data);
                }

                adapter = new NoticeAdapter(DeleteNoticeActivity.this, list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                deleteNoticeRecyclerview.setAdapter(adapter);
                if (list.size() == 0) {
                    noNotice.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DeleteNoticeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}