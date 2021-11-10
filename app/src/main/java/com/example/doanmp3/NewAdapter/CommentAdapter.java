package com.example.doanmp3.NewAdapter;

import static android.graphics.Typeface.BOLD;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Interface.ItemClick;
import com.example.doanmp3.NewModel.Comment;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context context;
    ArrayList<Comment> comments;
    ItemClick itemClick;
    FirebaseUser user;

    public CommentAdapter(Context context, ArrayList<Comment> comments, FirebaseUser user) {
        this.context = context;
        this.comments = comments;
        this.user = user;
    }

    public CommentAdapter(Context context, ArrayList<Comment> comments, FirebaseUser user, ItemClick itemClick) {
        this.context = context;
        this.comments = comments;
        this.itemClick = itemClick;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        SpannableStringBuilder username = Tools.SetTextStyle(comment.getNameUser(), BOLD);
        String content = comment.getContent();
        String timeAgo = Tools.CalculateTimeAgo(context, comment.getCreatedAt());
        List<String> likes = comment.getLiked();

        if (likes != null && likes.size() > 0) {
            String liked = likes.size() + " " + context.getApplicationContext().getString(R.string.like);
            holder.tvLike.setText(liked);
            if (likes.contains(user.getUid())) {
                holder.tvLike.setTextColor(R.color.love);
            } else {
                holder.tvLike.setTextColor(R.color.pharlap);
            }
        }


        holder.tvContent.setText(username);
        holder.tvContent.append("  " + content);
        holder.tvTimeAgo.setText(timeAgo);
        Glide.with(context).load(comment.getThumbnailUser())
                .error(R.drawable.person)
                .into(holder.imgUser);

        holder.tvLike.setOnClickListener(v -> {
            if(itemClick != null)
                itemClick.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        if (comments != null)
            return comments.size();
        return 0;
    }

    public void onItemChange(Comment comment) {
        if (comment == null || comments == null)
            return;

        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getId().equals(comment.getId())) {
                comments.remove(i);
                comments.add(i, comment);
                notifyItemChanged(i);
                return;
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent, tvTimeAgo, tvLike;
        CircleImageView imgUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTimeAgo = itemView.findViewById(R.id.tv_time_comment);
            tvLike = itemView.findViewById(R.id.tv_like_comment);
            imgUser = itemView.findViewById(R.id.img_avatar_user);
        }
    }


}
