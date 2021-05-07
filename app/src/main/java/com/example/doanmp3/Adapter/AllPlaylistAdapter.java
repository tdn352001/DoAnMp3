package com.example.doanmp3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllPlaylistAdapter extends RecyclerView.Adapter<AllPlaylistAdapter.ViewHolder> {
    Context context;
    ArrayList<Playlist> arrayList;

    public AllPlaylistAdapter(Context context, ArrayList<Playlist> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_all_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = arrayList.get(position);
        Picasso.with(context).load(playlist.getHinhAnh().toString()).into(holder.imageView);
        holder.txtTenPlaylist.setText(playlist.getTen().toString());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView txtTenPlaylist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_playlist_all);
            txtTenPlaylist = itemView.findViewById(R.id.txt_playlist_all);
        }
    }
}
