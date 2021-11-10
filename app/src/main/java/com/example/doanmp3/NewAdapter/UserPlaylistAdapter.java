package com.example.doanmp3.NewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmp3.NewModel.Playlist;
import com.example.doanmp3.R;
import com.example.doanmp3.Interface.ItemConfig;
import com.example.doanmp3.Interface.OptionItemClick;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class UserPlaylistAdapter extends RecyclerView.Adapter<UserPlaylistAdapter.ViewHolder> {

    Context context;
    ArrayList<Playlist> playlists;
    OptionItemClick itemClick;
    Boolean haveConfig;
    ItemConfig itemConfig;

    public UserPlaylistAdapter(Context context, ArrayList<Playlist> playlists, OptionItemClick itemClick) {
        this.context = context;
        this.playlists = playlists;
        this.itemClick = itemClick;
        this.haveConfig = false;
    }

    public UserPlaylistAdapter(Context context, ArrayList<Playlist> playlists, OptionItemClick itemClick, ItemConfig itemConfig) {
        this.context = context;
        this.playlists = playlists;
        this.itemClick = itemClick;
        this.itemConfig = itemConfig;
        this.haveConfig = true;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_user_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        if(playlist == null){
            return;
        }

        if(haveConfig){
            itemConfig.CustomItem(holder.itemView, position);
        }
        holder.tvPlaylistName.setText(playlist.getName());
        Glide.with(context).load(playlist.getThumbnail()).into(holder.thumbnail);
        holder.btnOptions.setOnClickListener(v -> itemClick.onOptionClick(position));
        holder.itemView.setOnClickListener(v -> itemClick.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        if(playlists != null)
            return  playlists.size();
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView thumbnail;
        TextView tvPlaylistName;
        MaterialButton btnOptions;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail_user_playlist_name);
            tvPlaylistName = itemView.findViewById(R.id.tv_user_playlist_name);
            btnOptions = itemView.findViewById(R.id.options_user_playlist_name);
        }
    }
}
