package com.example.onlinevoting.users.ui.election;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinevoting.R;
import com.example.onlinevoting.admin.candidates.ViewCandidateActivity;
import com.example.onlinevoting.models.CandidateModel;
import com.example.onlinevoting.models.VotersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ElectionAdapter extends RecyclerView.Adapter<ElectionAdapter.ElectionViewHolder>{

    private Context context;
    private List<CandidateModel> list;

    public ElectionAdapter(Context context, List<CandidateModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ElectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.election_item, parent, false);
        return new ElectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ElectionViewHolder holder, int position) {

        CandidateModel model = list.get(position);

        holder.pName.setText(model.getPartyName());
        holder.cName.setText(model.getName());
        Picasso.get().load(model.getProfileImage()).placeholder(R.drawable.profile).into(holder.profileImage);
        Picasso.get().load(model.getPartyImage()).placeholder(R.drawable.profile).into(holder.partyImage);
        holder.btnVote.setVisibility(View.VISIBLE);

        holder.btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIFAlreadyVoted(FirebaseAuth.getInstance().getUid(), model.getId());
            }
        });
    }

    private void checkIFAlreadyVoted(String voterID, String candidateID) {

        FirebaseDatabase.getInstance().getReference().child("votersVote").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(voterID).exists()) {
                    Toast.makeText(context, "Already Voted", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                    alertDialog.setTitle("Click on Yes to confirm you VOTE");

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            insertData(FirebaseAuth.getInstance().getUid(), candidateID);
                        }
                    });

                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertData(String voterID, String candidateID) {

        FirebaseDatabase.getInstance().getReference().child("votersVote").child(voterID).setValue(candidateID).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseDatabase.getInstance().getReference().child("result").child(candidateID).child(voterID).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Voted successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ElectionViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profileImage;
        private CircleImageView partyImage;
        private TextView cName;
        private TextView pName;
        private Button btnVote;

        public ElectionViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.e_profile_image);
            partyImage = itemView.findViewById(R.id.e_p_Image);
            cName = itemView.findViewById(R.id.e_name);
            pName = itemView.findViewById(R.id.e_p_name);
            btnVote = itemView.findViewById(R.id.btn_vote);
        }
    }
}
