package com.example.doanmp3.NewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Interface.ConfigItem;
import com.example.doanmp3.Interface.OptionItemClick;
import com.example.doanmp3.NewModel.Playlist;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder>{

    Context context;
    ArrayList<Playlist> playlists;
    OptionItemClick itemClick;
    ConfigItem configure;

    public PlaylistAdapter(Context context, ArrayList<Playlist> playlists, OptionItemClick itemClick) {
        this.context = context;
        this.playlists = playlists;
        this.itemClick = itemClick;
    }

    public PlaylistAdapter(Context context, ArrayList<Playlist> playlists, OptionItemClick itemClick, ConfigItem configure) {
        this.context = context;
        this.playlists = playlists;
        this.itemClick = itemClick;
        this.configure = configure;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        if(playlist == null){
            return;
        }

        if(configure != null){
            configure.configItem(holder.itemView, position);
        }
        holder.tvPlaylistName.setText(playlist.getName());
        Glide.with(context).load(playlist.getThumbnail()).into(holder.thumbnail);
        holder.itemView.setOnClickListener(v -> itemClick.onItemClick(position));
        holder.btnOptions.setOnClickListener(v -> itemClick.onOptionClick(position));
    }

    @Override
    public int getItemCount() {
        if(playlists != null)
            return  playlists.size();
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView thumbnail;
        TextView tvPlaylistName;
        MaterialButton btnOptions;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail_item_playlist);
            tvPlaylistName = itemView.findViewById(R.id.name_playlist_item_playlist);
            btnOptions = itemView.findViewById(R.id.options_item_playlist);
        }
    }


}
