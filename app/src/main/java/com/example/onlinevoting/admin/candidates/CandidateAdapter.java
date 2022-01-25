package com.example.onlinevoting.admin.candidates;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinevoting.R;
import com.example.onlinevoting.models.CandidateModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.CandidateViewAdapter>{

    private Context context;
    private List<CandidateModel> list;

    public CandidateAdapter(Context context, List<CandidateModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CandidateViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidates_list, parent, false);
        return new CandidateViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewAdapter holder, int position) {

        final CandidateModel model = list.get(position);

        holder.candidateName.setText(model.getName());
        holder.partyName.setText(model.getPartyName());

        Picasso.get().load(model.getProfileImage()).into(holder.imgProfile);

        holder.btnView.setVisibility(View.VISIBLE);
        holder.deny.setVisibility(View.VISIBLE);

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewCandidateActivity.class);
                intent.putExtra("name", model.getName());
                intent.putExtra("partyName", model.getPartyName());
                intent.putExtra("email", model.getEmail());
                intent.putExtra("mobile", model.getPhone());
                intent.putExtra("profileImage", model.getProfileImage());
                intent.putExtra("partyImage", model.getPartyImage());
                intent.putExtra("isApproved", model.getIsApproved());
                intent.putExtra("id", model.getId());
                intent.putExtra("aadhaarFront", model.getAadhaarFront());
                intent.putExtra("aadhaarBack", model.getAadhaarBack());
                context.startActivity(intent);
            }
        });

        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                alertDialog.setTitle("Do you want to Delete the Candidate?");

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("candidate").child(model.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Candidate Request Deleted.", Toast.LENGTH_SHORT).show();
                                    addNotification(model.getId(), "Your Request is Rejected");
                                }
                            }
                        });

                    }
                });

                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CandidateViewAdapter extends RecyclerView.ViewHolder {

        public CircleImageView imgProfile;
        public TextView candidateName;
        public TextView partyName;
        public Button btnView;
        public ImageView deny;

        public CandidateViewAdapter(@NonNull View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.candidate_image_profile);
            candidateName = itemView.findViewById(R.id.candidate_name);
            partyName = itemView.findViewById(R.id.party_name);
            btnView = itemView.findViewById(R.id.view_profile);
            deny = itemView.findViewById(R.id.deny_c);
        }
    }

    private void addNotification(String voteId, String msq) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("userid",voteId);
        map.put("text", msq);

        FirebaseDatabase.getInstance().getReference().child("Notification").child(voteId).push().setValue(map);
    }
}
