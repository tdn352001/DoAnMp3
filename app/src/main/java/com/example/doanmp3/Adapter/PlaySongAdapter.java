package com.example.doanmp3.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.PlayNhacActivity;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaySongAdapter extends RecyclerView.Adapter<PlaySongAdapter.ViewHolder> {

    private int row_index;

    PlayNhacActivity context;
    ArrayList<BaiHat> arrayList;

    public PlaySongAdapter(Context context, ArrayList<BaiHat> arrayList) {
        this.context = (PlayNhacActivity) context;
        this.arrayList = arrayList;
        row_index = -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.song_with_barvisualizer, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull PlaySongAdapter.ViewHolder holder, int position) {
        BaiHat baiHat = arrayList.get(position);
        if(!context.isAudio)
            Picasso.with(context).load(baiHat.getHinhBaiHat()).error(R.drawable.song).into(holder.imageView);
        else
        {
            holder.imageView.setImageResource(R.drawable.ic_song);
            holder.imageView.setBackgroundColor(R.color.black);
        }
        holder.TenCaSi.setText(baiHat.getTenAllCaSi());
        holder.TenBaiHat.setText(baiHat.getTenBaiHat());


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                context.changePos(position);
                notifyDataSetChanged();

            }
        });

        if (row_index == position) {
            holder.relativeLayout.setBackgroundResource(R.drawable.custom_selected_item_recycleview);
        } else {
            holder.relativeLayout.setBackgroundResource(R.drawable.custom_none_background);
        }

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView TenBaiHat, TenCaSi;
        RelativeLayout relativeLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_song_bvs);
            TenBaiHat = itemView.findViewById(R.id.txt_song_bvs);
            TenCaSi = itemView.findViewById(R.id.txt_song_casi_bvs);
            relativeLayout = itemView.findViewById(R.id.relavtive_song_bvs);


        }


    }

    public void changeRowSelected(int newIndex) {
        row_index = newIndex;
        notifyDataSetChanged();
    }


}
