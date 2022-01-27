package com.example.onlinevoting.users.ui.election;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlinevoting.R;
import com.example.onlinevoting.models.CandidateModel;
import com.example.onlinevoting.models.ElectionModel;
import com.example.onlinevoting.models.VotersModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ElectionFragment extends Fragment {

    private RecyclerView electionRecyclerview;
    private ProgressBar progressBar;
    private ArrayList<CandidateModel> list;
    private ElectionAdapter adapter;

    private TextView noElection;

    private DatabaseReference refElection, refCandidate;

    private String name, email, number, profileImage, voterIDImage, id, voterId;

    private String internetDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_election, container, false);

        electionRecyclerview = view.findViewById(R.id.recycler_view_candidates);
        progressBar = view.findViewById(R.id.progress_bar_e);
        noElection = view.findViewById(R.id.txt_election);

        refElection = FirebaseDatabase.getInstance().getReference().child("Election");
        refCandidate = FirebaseDatabase.getInstance().getReference().child("candidate");

        electionRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        electionRecyclerview.setHasFixedSize(true);

        getDate();

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

    private void getDate() {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="http://worldtimeapi.org/api/timezone/Asia/Kolkata";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String result = jsonObject.getString("datetime");
                            result = result.substring(0, 10);

                            internetDate = result.substring(8, 10) + "-" + result.substring(5, 7) + "-" + result.substring(0, 4);

                            checkElection();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noElection.setText(error.getMessage());
                noElection.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
        queue.add(stringRequest);
    }

    private void checkElection() {

        refElection.child(internetDate).child("electionData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ElectionModel electionModel = snapshot.getValue(ElectionModel.class);

                progressBar.setVisibility(View.GONE);
                if (electionModel != null) {
                    getData();
                }
                 else  {
                    noElection.setText(R.string.no_election_at_this_time);
                    noElection.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getData() {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.show();

        refCandidate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CandidateModel model = dataSnapshot.getValue(CandidateModel.class);

                    if (model.getIsApproved().equals("Yes")) {
                        list.add(0, model);
                    }

                    adapter = new ElectionAdapter(getContext(), list);
                    adapter.notifyDataSetChanged();
                    electionRecyclerview.setAdapter(adapter);
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkProfile() {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.show();

        FirebaseDatabase.getInstance().getReference().child("voters").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                VotersModel model = snapshot.getValue(VotersModel.class);

                String isApproved = model.getIsVerified();

                if (isApproved.equals("Yes")) {
                    pd.dismiss();
                    id = model.getId();
                    name = model.getName();
                    email = model.getEmail();
                    number = model.getPhone();
                    profileImage = model.getProfileImage();
                    voterIDImage = model.getVoterIDImage();
                    voterId = model.getVoterID();
                    checkIfApplied();
                } else {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Your Account is not Approved Yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkIfApplied() {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading");
        pd.show();

        FirebaseDatabase.getInstance().getReference().child("candidate").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CandidateModel model = snapshot.getValue(CandidateModel.class);
                if (snapshot.getChildrenCount() == 0) {
                    Intent intent = new Intent(getContext(), ApplyCandidateActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("mobile", number);
                    intent.putExtra("profileImage", profileImage);
                    intent.putExtra("voterIDImage", voterIDImage);
                    intent.putExtra("voterID", voterId);
                    intent.putExtra("id", id);
                    getContext().startActivity(intent);
                } else {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Already Applied", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}