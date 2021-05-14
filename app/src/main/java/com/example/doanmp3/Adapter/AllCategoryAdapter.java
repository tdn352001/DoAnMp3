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
import com.example.doanmp3.Service.DataService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.ViewHolder > {
    Context context;
    ArrayList<ChuDeTheLoai> arrayList;
    boolean isChuDe;

    public AllCategoryAdapter(Context context, ArrayList<ChuDeTheLoai> arrayList, boolean ischude) {
        this.context = context;
        this.arrayList = arrayList;
        isChuDe = ischude;
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
        Picasso.with(context).load(category.getHinh()).into(holder.imageView);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DanhSachBaiHatActivity.class);
                    if(isChuDe)
                        intent.putExtra("ChuDe", arrayList.get(getPosition()));
                    else
                        intent.putExtra("TheLoai", arrayList.get(getPosition()));

                    context.startActivity(intent);

                }
            });
        }
    }
}
