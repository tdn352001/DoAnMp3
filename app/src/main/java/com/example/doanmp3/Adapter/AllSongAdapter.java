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

import com.example.doanmp3.Activity.PlayActivity;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllSongAdapter extends RecyclerView.Adapter<AllSongAdapter.ViewHolder> {
    Context context;
    ArrayList<BaiHat> arrayList;

    public AllSongAdapter(Context context, ArrayList<BaiHat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_all_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = arrayList.get(position);
        Picasso.with(context).load(baiHat.getHinhBaiHat()).into(holder.imageView);
        holder.TenCaSi.setText(baiHat.getCaSi().toString());
        holder.TenBaiHat.setText(baiHat.getTenBaiHat());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView TenBaiHat, TenCaSi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_song_all);
            TenBaiHat = itemView.findViewById(R.id.txt_song_all);
            TenCaSi = itemView.findViewById(R.id.txt_song_casi_all);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayActivity.class);
                    intent.putExtra("baihat", arrayList.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
