package com.example.doanmp3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Context.Data.AudioThumbnail;
import com.example.doanmp3.Interface.ConfigItem;
import com.example.doanmp3.Interface.OptionItemClick;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {

    Context context;
    ArrayList<Song> songs;
    OptionItemClick optionItemClick;
    ConfigItem configItem;

    public AudioAdapter(Context context, ArrayList<Song> songs, OptionItemClick optionItemClick) {
        this.context = context;
        this.songs = songs;
        this.optionItemClick = optionItemClick;
    }

    public AudioAdapter(Context context, ArrayList<Song> songs, OptionItemClick optionItemClick, ConfigItem configItem) {
        this.context = context;
        this.songs = songs;
        this.optionItemClick = optionItemClick;
        this.configItem = configItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);
        if (song == null) {
            return;
        }
        if (configItem != null) {
            configItem.configItem(holder.itemView, position);
        }

        holder.tvSongName.setText(song.getName());
        holder.tvSingersName.setText(song.getAllSingerNames());
        try {
            int resId = Integer.parseInt(song.getThumbnail());
            holder.thumbnail.setImageResource(resId);

        } catch (Exception e) {
            holder.thumbnail.setImageResource(AudioThumbnail.getRandomThumbnail());
        }

        if (optionItemClick != null) {
            holder.itemView.setOnClickListener(v -> optionItemClick.onItemClick(position));
            holder.btnOptions.setOnClickListener(v -> optionItemClick.onOptionClick(position));
        }
    }

    @Override
    public int getItemCount() {
        if (songs != null)
            return songs.size();
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView thumbnail;
        TextView tvSongName, tvSingersName;
        MaterialButton btnOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail_item_song);
            tvSongName = itemView.findViewById(R.id.name_song_item_song);
            tvSingersName = itemView.findViewById(R.id.name_singers_item_song);
            btnOptions = itemView.findViewById(R.id.options_item_song);
        }
    }
}
