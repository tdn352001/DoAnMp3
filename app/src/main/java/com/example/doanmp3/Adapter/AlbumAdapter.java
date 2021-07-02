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

import com.bumptech.glide.Glide;
import com.example.doanmp3.Activity.DanhSachBaiHatActivity;
import com.example.doanmp3.Model.Album;
import com.example.doanmp3.R;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    Context context;
    ArrayList<Album> arrayList;
    boolean viewmore;

    public AlbumAdapter(Context context, ArrayList<Album> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        viewmore = false;
    }

    public AlbumAdapter(Context context, ArrayList<Album> arrayList, boolean viewmore) {
        this.context = context;
        this.arrayList = arrayList;
        this.viewmore = viewmore;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_all_album, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = arrayList.get(position);
        holder.TenAlbum.setText(album.getTenAlbum());
        holder.TenCaSi.setText(album.getTenCaSi());
        Glide.with(context).load(album.getHinhAlbum()).error(R.drawable.song).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            if (viewmore && arrayList.size() > 4) {
                return 4;
            } else
                return arrayList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView TenCaSi, TenAlbum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_album_all);
            TenAlbum = itemView.findViewById(R.id.txt_album_all);
            TenCaSi = itemView.findViewById(R.id.txt_album_casi_all);


            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DanhSachBaiHatActivity.class);
                intent.putExtra("album", arrayList.get(getPosition()));
                context.startActivity(intent);
            });
        }
    }
}
