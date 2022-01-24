package com.example.onlinevoting.users.ui.notice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class NotificationFragment extends Fragment {

    private RecyclerView noticeRecyclerview;
    private ProgressBar progressBar;
    private ArrayList<NoticeModel> list;
    private NoticeAdapter adapter;

    private TextView noNotice;

    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        noticeRecyclerview = view.findViewById(R.id.recycler_view_notice);
        progressBar = view.findViewById(R.id.progress_bar_v);
        noNotice = view.findViewById(R.id.txt_notice);

        reference = FirebaseDatabase.getInstance().getReference().child("Notice");

        noticeRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        noticeRecyclerview.setHasFixedSize(true);

        getNotice();

        return view;
    }

    private void getNotice() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NoticeModel data = dataSnapshot.getValue(NoticeModel.class);
                    list.add(0,data);
                }

                adapter = new NoticeAdapter(list, getContext());
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                noticeRecyclerview.setAdapter(adapter);
                if (list.size() == 0) {
                    noNotice.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}