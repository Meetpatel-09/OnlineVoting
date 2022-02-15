package com.example.onlinevoting.admin.election;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
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
import com.example.onlinevoting.admin.notice.DeleteNoticeActivity;
import com.example.onlinevoting.admin.notice.NoticeAdapter;
import com.example.onlinevoting.models.CandidateModel;
import com.example.onlinevoting.models.ElectionModel;
import com.example.onlinevoting.models.NoticeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewResultsActivity extends AppCompatActivity {

    private RecyclerView viewResultsRecyclerview;
    private ProgressBar progressBar;
    private ArrayList<CandidateModel> list;
    private ViewResultAdapter adapter;

    private TextView noResult;

    private DatabaseReference refElection, reference;

    private String internetDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);

        viewResultsRecyclerview = findViewById(R.id.view_results_recyclerview);
        progressBar = findViewById(R.id.progress_bar_r);

        noResult = findViewById(R.id.no_results);

        refElection = FirebaseDatabase.getInstance().getReference().child("Election");
        reference = FirebaseDatabase.getInstance().getReference().child("candidate");

        viewResultsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        viewResultsRecyclerview.setHasFixedSize(true);

        getResults();
    }

//    private void getDate() {
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="http://worldtimeapi.org/api/timezone/Asia/Kolkata";
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            String result = jsonObject.getString("datetime");
//                            result = result.substring(0, 10);
//
//                            internetDate = result.substring(8, 10) + "-" + result.substring(5, 7) + "-" + result.substring(0, 4);
//
//                            checkElection();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                noResult.setText(error.getMessage());
//                noResult.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//        queue.add(stringRequest);
//    }


//    private void checkElection() {
//
//        refElection.child(internetDate).child("electionData").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ElectionModel electionModel = snapshot.getValue(ElectionModel.class);
//
//                progressBar.setVisibility(View.GONE);
//                if (electionModel != null) {
//                    getResults();
//                }
//                else  {
//                    noResult.setText(R.string.no_election_at_this_time);
//                    noResult.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void getResults() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CandidateModel model = dataSnapshot.getValue(CandidateModel.class);

                    if (model.getIsApproved().equals("Yes")) {
                        list.add(model);
                    }
                }

                adapter = new ViewResultAdapter(ViewResultsActivity.this, list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                viewResultsRecyclerview.setAdapter(adapter);
                if (list.size() == 0) {
                    noResult.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewResultsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}