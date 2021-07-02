package com.example.doanmp3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.DetailSingerActivity;
import com.example.doanmp3.Model.CaSi;
import com.example.doanmp3.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchCaSiAdapter extends RecyclerView.Adapter<SearchCaSiAdapter.ViewHolder>{
    Context context;
    ArrayList<CaSi> arrayList;

    public SearchCaSiAdapter(Context context, ArrayList<CaSi> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_user_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        CaSi casi = arrayList.get(position);
        Glide.with(context).load(casi.getHinhCaSi()).error(R.drawable.song).error(R.drawable.karaoke_mic).into(holder.imageView);
        holder.TenCaSi.setText(casi.getTenCaSi());
    }

    @Override
    public int getItemCount() {
        if (arrayList != null)
            return arrayList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView TenCaSi;
        RoundedImageView imageView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            TenCaSi = itemView.findViewById(R.id.txt_ten_playlist_user);
            imageView = itemView.findViewById(R.id.img_user_playlist);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailSingerActivity.class);
                intent.putExtra("CaSi",  arrayList.get(getPosition()));
                context.startActivity(intent);
            });
        }
    }
}
