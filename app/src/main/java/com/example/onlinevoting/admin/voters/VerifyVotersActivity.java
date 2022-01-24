package com.example.onlinevoting.admin.voters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.onlinevoting.R;
import com.example.onlinevoting.models.NoticeModel;
import com.example.onlinevoting.models.VotersModel;
import com.example.onlinevoting.users.ui.notice.NoticeAdapter;
import com.google.firebase.database.DatabaseReference;

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


    }
}