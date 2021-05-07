package com.example.doanmp3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Model.CaSi;
import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.ViewHolder>{

    Context context;
    ArrayList<CaSi> arrayList;

    public SingerAdapter(Context context, ArrayList<CaSi> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public SingerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_singer, parent, false);


        return new SingerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingerAdapter.ViewHolder holder, int position) {
        CaSi casi= arrayList.get(position);
        holder.txtPlaylist.setText(casi.getTenCaSi());
        Picasso.with(context).load(casi.getHinhCaSi()).into(holder.imageView);
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView txtPlaylist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_casi);
            txtPlaylist = itemView.findViewById(R.id.txt_casi);
        }
    }
}
