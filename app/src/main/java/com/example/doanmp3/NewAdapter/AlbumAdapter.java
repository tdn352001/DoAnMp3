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
import com.example.doanmp3.NewModel.Album;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class AlbumAdapter extends  RecyclerView.Adapter<AlbumAdapter.ViewHolder>{

    Context context;
    ArrayList<Album> albums;
    ItemClick itemClick;
    Boolean haveConfig;
    ConfigItem configure;


    public AlbumAdapter(Context context, ArrayList<Album> albums, ItemClick itemClick) {
        this.context = context;
        this.albums = albums;
        haveConfig = false;
        this.itemClick = itemClick;
    }

    public AlbumAdapter(Context context, ArrayList<Album> albums, ItemClick itemClick, ConfigItem configure) {
        this.context = context;
        this.albums = albums;
        this.itemClick = itemClick;
        haveConfig = true;
        this.configure = configure;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albums.get(position);
        if(album == null){
            return;
        }

        if(haveConfig){
            configure.configure(holder.itemView, position);
        }
        holder.tvAlbumName.setText(album.getName());
        holder.tvSingersName.setText(album.getAllSingerNames());
        Glide.with(context).load(album.getThumbnail()).into(holder.thumbnail);
        holder.btnOptions.setOnClickListener(v -> itemClick.optionClick(position));
        holder.itemView.setOnClickListener(v -> itemClick.itemClick(position));
    }

    @Override
    public int getItemCount() {
        if(albums != null)
            return  albums.size();
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView thumbnail;
        TextView tvAlbumName, tvSingersName;
        MaterialButton btnOptions;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail_item_album);
            tvAlbumName = itemView.findViewById(R.id.name_song_item_album);
            tvSingersName = itemView.findViewById(R.id.name_singers_item_album);
            btnOptions = itemView.findViewById(R.id.options_item_album);
        }
    }

    public interface ItemClick {
        void itemClick(int position);
        void optionClick(int position);
    }

    public interface ConfigItem{
        void configure(View itemView, int position);
    }
}
