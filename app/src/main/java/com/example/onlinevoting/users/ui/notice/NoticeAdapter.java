package com.example.onlinevoting.users.ui.notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinevoting.R;
import com.example.onlinevoting.models.NoticeModel;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>{

    private List<NoticeModel> list;
    private Context context;

    public NoticeAdapter(List<NoticeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.v_news_feed_item_layout, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {

        NoticeModel data = list.get(position);

        holder.noticeTitle.setText(data.getTitle());
        holder.date.setText(data.getDate());
        holder.time.setText(data.getTime());

        try {
            if (data.getImage() != null)
                Picasso.get().load(data.getImage()).into(holder.noticeImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder {

        private TextView noticeTitle, date, time;
        private ImageView noticeImage;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);

            noticeTitle = itemView.findViewById(R.id.s_notice_title);
            date = itemView.findViewById(R.id.s_date);
            time = itemView.findViewById(R.id.s_time);
            noticeImage = itemView.findViewById(R.id.s_notice_image);
        }
    }
}
