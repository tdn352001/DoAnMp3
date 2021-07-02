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
import com.example.doanmp3.Model.ChuDeTheLoai;
import com.example.doanmp3.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.ViewHolder > {
    Context context;
    ArrayList<ChuDeTheLoai> arrayList;
    int Loai;
    int TongChuDe;



    public AllCategoryAdapter(Context context, ArrayList<ChuDeTheLoai> arrayList, int loai) {
        this.context = context;
        this.arrayList = arrayList;
        Loai = loai;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_all_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChuDeTheLoai category = arrayList.get(position);
        Picasso.with(context).load(category.getHinh()).error(R.drawable.song).into(holder.imageView);
        holder.Ten.setText(category.getTen());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView Ten;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_categoty_all);
            Ten = itemView.findViewById(R.id.txt_categoty_all);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DanhSachBaiHatActivity.class);
                if(Loai == 1 || (Loai == 3 && getPosition() < arrayList.size() - TongChuDe))
                    intent.putExtra("ChuDe", arrayList.get(getPosition()));
                else
                    intent.putExtra("TheLoai", arrayList.get(getPosition()));


                context.startActivity(intent);

            });
        }
    }
    public int getTongChuDe() {
        return TongChuDe;
    }

    public void setTongChuDe(int tongChuDe) {
        TongChuDe = tongChuDe;
    }
}
