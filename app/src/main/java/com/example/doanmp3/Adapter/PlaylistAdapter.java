package com.example.doanmp3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.DanhSachBaiHatActivity;
import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder>{

    Context context;
    ArrayList<Playlist> arrayList;
    boolean viewmore;

    public PlaylistAdapter(Context context, ArrayList<Playlist> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        viewmore = false;
    }

    public PlaylistAdapter(Context context, ArrayList<Playlist> arrayList, boolean viewmore) {
        this.context = context;
        this.arrayList = arrayList;
        this.viewmore = viewmore;
    }

    @NonNull
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_all_playlist, parent, false);
        return new PlaylistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.ViewHolder holder, int position) {
        Playlist playlist = arrayList.get(position);
        Picasso.with(context).load(playlist.getHinhAnh().toString()).error(R.drawable.song).into(holder.imageView);
        holder.txtTenPlaylist.setText(playlist.getTen().toString());
    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            if (viewmore && arrayList.size() > 4) {
                return 4;
            }
            else
                return  arrayList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView txtTenPlaylist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_playlist_all);
            txtTenPlaylist = itemView.findViewById(R.id.txt_playlist_all);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DanhSachBaiHatActivity.class);
                    intent.putExtra("playlist", arrayList.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
