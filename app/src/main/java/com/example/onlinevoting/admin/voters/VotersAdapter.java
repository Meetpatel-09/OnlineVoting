package com.example.onlinevoting.admin.voters;

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
import com.example.onlinevoting.models.VotersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VotersAdapter extends RecyclerView.Adapter<VotersAdapter.VotersViewAdpater> {

    private Context context;
    private List<VotersModel> list;

    public VotersAdapter(Context context, List<VotersModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VotersViewAdpater onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.voters_list, parent, false);
        return new VotersViewAdpater(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VotersViewAdpater holder, int position) {

        final VotersModel model = list.get(position);

        if (model.getIsVerified().equals("Yes")) {
            holder.btnAccept.setText("Disapprove");
        } else {
            holder.btnAccept.setText("Approve");
        }

        holder.voterName.setText(model.getName());
        holder.btnAccept.setVisibility(View.VISIBLE);
        Picasso.get().load(model.getProfileImage()).into(holder.imgProfile);
        holder.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewVoterProfileActivity.class);
                intent.putExtra("name", model.getName());
                intent.putExtra("email", model.getEmail());
                intent.putExtra("mobile", model.getPhone());
                intent.putExtra("profileImage", model.getProfileImage());
                intent.putExtra("voterIDImage", model.getVoterIDImage());
                intent.putExtra("voterID", model.getVoterID());
                intent.putExtra("isApproved", model.getIsVerified());
                intent.putExtra("id", model.getId());
                context.startActivity(intent);
            }
        });

        holder.voterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewVoterProfileActivity.class);
                intent.putExtra("name", model.getName());
                intent.putExtra("email", model.getEmail());
                intent.putExtra("mobile", model.getPhone());
                intent.putExtra("profileImage", model.getProfileImage());
                intent.putExtra("voterIDImage", model.getVoterIDImage());
                intent.putExtra("voterID", model.getVoterID());
                intent.putExtra("isApproved", model.getIsVerified());
                intent.putExtra("id", model.getId());
                context.startActivity(intent);
            }
        });

//        holder.deny.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//
//                alertDialog.setTitle("Do you want to reject Voter?");
//
//                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        FirebaseDatabase.getInstance().getReference().child("voters").child(model.getId()).child("isVerified").setValue("No").addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(context, "Voter Rejected.", Toast.LENGTH_SHORT).show();
//                                    addNotification2(model.getId());
//                                }
//                            }
//                        });
//
//                    }
//                });
//
//                alertDialog.show();
//            }
//        });

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                if (holder.btnAccept.getText().equals("Approve")) {
                    alertDialog.setTitle("Do you want to Approve Voter?");
                } else {
                    alertDialog.setTitle("Do you want to Reject Voter?");
                }

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (holder.btnAccept.getText().equals("Approve")) {
                            FirebaseDatabase.getInstance().getReference().child("voters").child(model.getId()).child("isVerified").setValue("Yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Voter Approved.", Toast.LENGTH_SHORT).show();
                                        addNotification(model.getId(), "Your Account is Approved");
                                    }
                                }
                            });
                        } else {
                            FirebaseDatabase.getInstance().getReference().child("voters").child(model.getId()).child("isVerified").setValue("No").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Voter Disapproved.", Toast.LENGTH_SHORT).show();
                                        addNotification(model.getId(), "Your Account is Disapproved");
                                    }
                                }
                            });
                        }
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

    public class VotersViewAdpater extends RecyclerView.ViewHolder{

        public CircleImageView imgProfile;
        public TextView voterName;
        public Button btnAccept;
//        public ImageView deny;

        public VotersViewAdpater(@NonNull View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.voter_image_profile);
            voterName = itemView.findViewById(R.id.voter_name);
            btnAccept = itemView.findViewById(R.id.btn_approve);
//            deny = itemView.findViewById(R.id.deny);
        }
    }

    private void addNotification(String voteId, String msq) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("userid",voteId);
        map.put("text", msq);

        FirebaseDatabase.getInstance().getReference().child("Notification").child(voteId).push().setValue(map);
    }
}
