package com.example.onlinevoting.users.ui.election;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinevoting.R;
import com.example.onlinevoting.StartActivity;
import com.example.onlinevoting.admin.voters.ViewVoterProfileActivity;
import com.example.onlinevoting.authentication.AdminLogInActivity;
import com.example.onlinevoting.models.NoticeModel;
import com.example.onlinevoting.models.VotersModel;
import com.example.onlinevoting.users.MainActivity;
import com.example.onlinevoting.users.ui.notice.NoticeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ElectionFragment extends Fragment {

    private RecyclerView electionRecyclerview;
    private ProgressBar progressBar;
//    private ArrayList<ElectionModel> list;
//    private ElectionAdapter adapter;

    private TextView noElection;

    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_election, container, false);

        electionRecyclerview = view.findViewById(R.id.recycler_view_notice);
        progressBar = view.findViewById(R.id.progress_bar_v);

        progressBar.setVisibility(getId());
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkProfile();
                fab.clearFocus();
            }
        });

        return view;
    }

    void checkProfile() {
        FirebaseDatabase.getInstance().getReference().child("voters").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                VotersModel model = snapshot.getValue(VotersModel.class);

                String isApproved = model.getIsVerified();

                if (isApproved.equals("Yes")) {
                    Intent intent = new Intent(getContext(), ApplyCandidateActivity.class);
                    intent.putExtra("name", model.getName());
                    intent.putExtra("email", model.getEmail());
                    intent.putExtra("mobile", model.getPhone());
                    intent.putExtra("profileImage", model.getProfileImage());
                    intent.putExtra("voterIDImage", model.getVoterIDImage());
                    intent.putExtra("voterID", model.getVoterID());
                    intent.putExtra("id", model.getId());
                    getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Your Account is not Approved Yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}