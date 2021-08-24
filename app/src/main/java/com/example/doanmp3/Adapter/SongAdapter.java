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
import com.example.doanmp3.Activity.PlayNhacActivity;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>{

    Context context;
    ArrayList<BaiHat> arrayList;
    boolean isFull;

    public SongAdapter(Context context, ArrayList<BaiHat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        isFull = false;
    }

    public SongAdapter(Context context, ArrayList<BaiHat> arrayList, boolean isFull) {
        this.context = context;
        this.arrayList = arrayList;
        this.isFull = isFull;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = arrayList.get(position);
        holder.txtStt.setText(position + 1 + "");
        holder.txtBaiHat.setText(baiHat.getTenBaiHat());
        holder.txtCaSi.setText("Son tung mtp");
        Glide.with(context).load(baiHat.getHinhBaiHat()).error(R.drawable.song).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(isFull)
            return arrayList.size();
        return 7;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView txtCaSi, txtBaiHat, txtStt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtStt = itemView.findViewById(R.id.txt_stt);
            imageView = itemView.findViewById(R.id.img_baihat);
            txtCaSi = itemView.findViewById(R.id.txt_baihat_casi);
            txtBaiHat= itemView.findViewById(R.id.txt_baihat);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, PlayNhacActivity.class);
                intent.putExtra("mangbaihat", arrayList);
                intent.putExtra("position", getPosition());
                DanhSachBaiHatActivity.category = "Playlist";
                DanhSachBaiHatActivity.TenCategoty="Bảng Xếp Hạng Bài Hát Yêu Thích";
                context.startActivity(intent);
            });
        }
    }


}
