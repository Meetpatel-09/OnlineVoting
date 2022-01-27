package com.example.onlinevoting.users.ui.election;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinevoting.R;
import com.example.onlinevoting.models.CandidateModel;

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

        public ElectionViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.e_profile_image);
            partyImage = itemView.findViewById(R.id.e_p_Image);
            cName = itemView.findViewById(R.id.e_name);
            pName = itemView.findViewById(R.id.e_p_name);
        }
    }
}