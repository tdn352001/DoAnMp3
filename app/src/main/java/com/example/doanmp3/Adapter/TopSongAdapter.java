package com.example.doanmp3.Adapter;

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
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class TopSongAdapter extends  RecyclerView.Adapter<TopSongAdapter.ViewHolder> {

    Context context;
    ArrayList<Song> songs;
    OptionItemClick itemClick;
    ConfigItem configItem;


    public TopSongAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    public TopSongAdapter(Context context, ArrayList<Song> songs, OptionItemClick itemClick) {
        this.context = context;
        this.songs = songs;
        this.itemClick = itemClick;
    }

    public TopSongAdapter(Context context, ArrayList<Song> songs, OptionItemClick itemClick, ConfigItem configItem) {
        this.context = context;
        this.songs = songs;
        this.itemClick = itemClick;
        this.configItem = configItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_top_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);
        if(song == null){
            return;
        }
        int stt = position + 1;
        String strStt = stt > 9 ? String.valueOf(stt) : "0" + stt;
        holder.tvStt.setText(strStt);
        holder.tvSongName.setText(song.getName());
        holder.tvSingersName.setText(song.getAllSingerNames());
        Glide.with(context).load(song.getThumbnail()).into(holder.thumbnail);

        if(itemClick != null){
            holder.btnOptions.setOnClickListener(v -> itemClick.onOptionClick(position));
            holder.itemView.setOnClickListener(v -> itemClick.onItemClick(position));
        }

        if(configItem != null){
            configItem.configItem(holder.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        if(songs == null)
            return 0;
        return songs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvStt;
        RoundedImageView thumbnail;
        TextView tvSongName, tvSingersName;
        MaterialButton btnOptions;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStt = itemView.findViewById(R.id.tv_stt);
            thumbnail = itemView.findViewById(R.id.thumbnail_item_song);
            tvSongName = itemView.findViewById(R.id.name_song_item_song);
            tvSingersName = itemView.findViewById(R.id.name_singers_item_song);
            btnOptions = itemView.findViewById(R.id.options_item_song);
        }
    }
}
