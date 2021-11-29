package com.example.doanmp3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Interface.ItemClick;
import com.example.doanmp3.Models.Genre;
import com.example.doanmp3.R;

import java.util.ArrayList;

public class GenreAdapter extends  RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    Context context;
    ArrayList<Genre> genres;
    ItemClick itemClick;

    public GenreAdapter(Context context, ArrayList<Genre> categories, ItemClick categoryClick) {
        this.context = context;
        this.genres = categories;
        this.itemClick = categoryClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Genre category = genres.get(position);
        if(category == null)
            return;

        holder.txtName.setText(category.getName());
        Glide.with(context).load(category.getThumbnail()).into(holder.thumbnail);
        holder.itemView.setOnClickListener(v -> itemClick.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        if(genres != null)
            return  genres.size();
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView txtName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.img_category);
            txtName = itemView.findViewById(R.id.txt_category);
        }
    }
}
