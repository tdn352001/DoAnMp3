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
import com.example.doanmp3.Model.ChuDeTheLoai;
import com.example.doanmp3.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    Context context;
    ArrayList<ChuDeTheLoai> arrayList;

    public CategoryAdapter(Context context, ArrayList<ChuDeTheLoai> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_category, parent, false);



        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        ChuDeTheLoai categorys = arrayList.get(position);
        holder.txtCategory.setText(categorys.getTen());
        Glide.with(context).load(categorys.getHinh()).error(R.drawable.song).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView txtCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_categoty);
            txtCategory = itemView.findViewById(R.id.txt_categoty);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DanhSachBaiHatActivity.class);
                if(getPosition() < 3){
                    setCategoryPlayActivity("Chủ Đề", arrayList.get(getPosition()).getTen());
                    intent.putExtra("ChuDe", arrayList.get(getPosition()));
                }
                else{
                    setCategoryPlayActivity("Chủ Đề", arrayList.get(getPosition()).getTen());
                    intent.putExtra("TheLoai", arrayList.get(getPosition()));
                }
                context.startActivity(intent);
            });


        }
    }

    private void setCategoryPlayActivity(String Loai, String Ten){
        DanhSachBaiHatActivity.TenCategoty = Ten;
        DanhSachBaiHatActivity.category = Loai;
    }
}

