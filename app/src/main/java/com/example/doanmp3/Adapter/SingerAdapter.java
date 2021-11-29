package com.example.doanmp3.Adapter;

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
import com.example.doanmp3.Models.Singer;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.ViewHolder>{

    Context context;
    ArrayList<Singer> singers;
    OptionItemClick itemClick;
    ConfigItem configure;

    public SingerAdapter(Context context, ArrayList<Singer> singers, OptionItemClick itemClick) {
        this.context = context;
        this.singers = singers;
        this.itemClick = itemClick;
    }

    public SingerAdapter(Context context, ArrayList<Singer> singers, OptionItemClick itemClick, ConfigItem configure) {
        this.context = context;
        this.singers = singers;
        this.itemClick = itemClick;
        this.configure = configure;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_singer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Singer singer = singers.get(position);
        if(singer == null){
            return;
        }

        if(configure != null){
            configure.configItem(holder.itemView, position);
        }
        holder.tvSingerName.setText(singer.getName());
        Glide.with(context).load(singer.getThumbnail()).into(holder.thumbnail);
        holder.itemView.setOnClickListener(v -> itemClick.onItemClick(position));
        holder.btnOptions.setOnClickListener(v -> itemClick.onOptionClick(position));
    }

    @Override
    public int getItemCount() {
        if(singers != null)
            return  singers.size();
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSingers(ArrayList<Singer> singers) {
        this.singers = singers;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView thumbnail;
        TextView tvSingerName;
        MaterialButton btnOptions;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail_item_singer);
            tvSingerName = itemView.findViewById(R.id.name_singer_item_singer);
            btnOptions = itemView.findViewById(R.id.options_item_singer);
        }
    }
}
