package com.example.doanmp3.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Interface.ItemClick;
import com.example.doanmp3.Models.Playlist;
import com.example.doanmp3.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class CategoryPlaylistAdapter extends RecyclerView.Adapter<CategoryPlaylistAdapter.ViewHolder> {

    Context context;
    ArrayList<Playlist> playlists;
    ItemClick itemClick;

    public CategoryPlaylistAdapter(Context context, ArrayList<Playlist> playlists, ItemClick itemClick) {
        this.context = context;
        this.playlists = playlists;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.txtName.setText(playlist.getName());
        Log.e("EEE", playlist.getThumbnail());
        Glide.with(context).load(playlist.getThumbnail()).into(holder.thumbnail);
        holder.itemView.setOnClickListener(v -> itemClick.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        if(playlists != null)
            return  playlists.size();
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView thumbnail;
        TextView txtName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail_playlist);
            txtName = itemView.findViewById(R.id.playlist_name);
        }
    }
}
