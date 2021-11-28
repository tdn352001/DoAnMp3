package com.example.doanmp3.NewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Interface.ItemChecked;
import com.example.doanmp3.Interface.Search;
import com.example.doanmp3.NewModel.Song;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.Tools;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class AddSongAdapter extends RecyclerView.Adapter<AddSongAdapter.ViewHolder> implements Filterable {

    Context context;
    ArrayList<Song> songs;
    ArrayList<Song> tempSongs;
    ArrayList<Song> addedSongs;
    boolean isAddedFragment;

    // Interface
    Search searchCompleted;
    ItemChecked itemChecked;

    public AddSongAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
        this.tempSongs = songs;
        isAddedFragment = false;
    }

    public AddSongAdapter(Context context, ArrayList<Song> songs, ArrayList<Song> tempSongs, ArrayList<Song> addedSongs) {
        this.context = context;
        this.songs = songs;
        this.tempSongs = tempSongs;
        this.addedSongs = addedSongs;
    }

    public AddSongAdapter(Context context, ArrayList<Song> songs, boolean isAddedFragment) {
        this.context = context;
        this.songs = songs;
        this.tempSongs = songs;
        this.isAddedFragment = isAddedFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_add_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.tvSongName.setText(song.getName());
        holder.tvSingersName.setText(song.getAllSingerNames());
        Glide.with(context).load(song.getThumbnail()).into(holder.thumbnail);

        if (isAddedFragment) {
            holder.cbAdded.setChecked(true);
        } else {
            holder.cbAdded.setChecked(Tools.isSongInSongs(song, addedSongs));
        }

        holder.itemView.setOnClickListener(v -> {
            // nếu là added fragment mặc định là false
            boolean isAdded = isAddedFragment || holder.cbAdded.isChecked();
            holder.cbAdded.setChecked(!isAdded);
            if (itemChecked != null) {
                itemChecked.onItemCheckedChange(!isAdded, song);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (songs != null)
            return songs.size();
        return 0;
    }

    public void setSearchCompleted(Search searchCompleted) {
        this.searchCompleted = searchCompleted;
    }

    public void setAddedSongs(ArrayList<Song> addedSongs) {
        this.addedSongs = addedSongs;
    }

    public void setItemChecked(ItemChecked itemChecked) {
        this.itemChecked = itemChecked;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = (String) constraint;
                if (query.equals("")) {
                    songs = tempSongs;
                } else {
                    ArrayList<Song> resultSongs = new ArrayList<>();
                    for (Song song : tempSongs) {
                        String nameSong = song.getName().toLowerCase();
                        String nameRemoveAccent = Tools.removeAccent(nameSong);
                        if (nameRemoveAccent.contains(query.toLowerCase())) {
                            resultSongs.add(song);
                        }
                    }
                    songs = resultSongs;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = songs;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (searchCompleted != null) {
                    searchCompleted.onSearchCompleted(songs.size());
                }
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView thumbnail;
        TextView tvSongName, tvSingersName;
        CheckBox cbAdded;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail_item_song);
            tvSongName = itemView.findViewById(R.id.name_song_item_song);
            tvSingersName = itemView.findViewById(R.id.name_singers_item_song);
            cbAdded = itemView.findViewById(R.id.cb_added);
        }
    }


}
