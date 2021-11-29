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
import com.example.doanmp3.NewModel.Song;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    Context context;
    ArrayList<Song> songs;
    OptionItemClick itemClick;
    ConfigItem configure;

    // Cài đặt có muốn ẩn bớt phần từ không
    boolean isViewMore;
    int quantityItemDisplay;

    public SongAdapter(Context context, ArrayList<Song> songs, OptionItemClick itemClick) {
        this.context = context;
        this.songs = songs;
        this.itemClick = itemClick;
        isViewMore = false;
        quantityItemDisplay = 0;
    }

    public SongAdapter(Context context, ArrayList<Song> songs, OptionItemClick itemClick, ConfigItem configure) {
        this.context = context;
        this.songs = songs;
        this.itemClick = itemClick;
        this.configure = configure;
        isViewMore = false;
        quantityItemDisplay = 0;
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

        if (configure != null) {
            configure.configItem(holder.itemView, position);
        }

        holder.tvSongName.setText(song.getName());
        holder.tvSingersName.setText(song.getAllSingerNames());
        Glide.with(context).load(song.getThumbnail()).into(holder.thumbnail);
        holder.itemView.setOnClickListener(v -> itemClick.onItemClick(position));
        holder.btnOptions.setOnClickListener(v -> itemClick.onOptionClick(position));
    }

    @Override
    public int getItemCount() {

        // Display Item less
        if (isViewMore) {
            return CountDisplayItem();
        }

        // Default
        if (songs != null)
            return songs.size();
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public boolean isViewMore() {
        return isViewMore;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setViewMore(boolean viewMore) {
        notifyDataSetChanged();
        isViewMore = viewMore;
    }

    public int getQuantityItemDisplay() {
        return quantityItemDisplay;
    }

    public void setQuantityItemDisplay(int quantityItemDisplay) {
        this.quantityItemDisplay = quantityItemDisplay;
    }

    private int CountDisplayItem() {
        if (songs == null)
            return 0;

        return Math.min(songs.size(), quantityItemDisplay);
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
