package com.example.onlinevoting.admin.election;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinevoting.R;
import com.example.onlinevoting.models.CandidateModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewResultAdapter extends RecyclerView.Adapter<ViewResultAdapter.ViewResultViewAdapter> {

    private Context context;
    private List<CandidateModel> list;

    public ViewResultAdapter(Context context, List<CandidateModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewResultViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.result_item, parent, false);
        return new ViewResultViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewResultViewAdapter holder, int position) {

        CandidateModel model = list.get(position);

        holder.pName.setText(model.getPartyName());
        holder.cName.setText(model.getName());
        Picasso.get().load(model.getProfileImage()).placeholder(R.drawable.profile).into(holder.profileImage);
        Picasso.get().load(model.getPartyImage()).placeholder(R.drawable.profile).into(holder.partyImage);

        getVotesCount(model.getId(), holder.votesTxt);

    }

    private void getVotesCount(String candidateID, TextView votesTxt) {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading");
        pd.show();

        FirebaseDatabase.getInstance().getReference().child("result").child(candidateID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() != 0) {
                    String votes = String.valueOf(snapshot.getChildrenCount());
                    votesTxt.setText(votes);
                } else {
                    votesTxt.setText("0");
                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewResultViewAdapter extends RecyclerView.ViewHolder {

        private CircleImageView profileImage;
        private CircleImageView partyImage;
        private TextView cName;
        private TextView pName;
        private TextView votesTxt;

        public ViewResultViewAdapter(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.r_profile_image);
            partyImage = itemView.findViewById(R.id.r_p_Image);
            cName = itemView.findViewById(R.id.r_c_name);
            pName = itemView.findViewById(R.id.r_p_name);
            votesTxt = itemView.findViewById(R.id.txt_votes);

        }
    }
}
