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

import com.example.doanmp3.Activity.PlayNhacActivity;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchSongAdapter extends RecyclerView.Adapter<SearchSongAdapter.ViewHolder> {

    Context context;
    ArrayList<BaiHat> arrayList;
    boolean IsSearch;
    public boolean ViewMore;

    public SearchSongAdapter(Context context, ArrayList<BaiHat> arrayList, boolean isSearch) {
        this.context = context;
        this.arrayList = arrayList;
        IsSearch = isSearch;
        ViewMore = false;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_search_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        BaiHat baiHat = arrayList.get(position);
        Picasso.with(context).load(baiHat.getHinhBaiHat()).into(holder.imageView);
        holder.TenBaiHat.setText(baiHat.getTenBaiHat());
        holder.TenCaSi.setText(baiHat.getTenAllCaSi());
    }

    @Override
    public int getItemCount() {
        if (arrayList != null)
            if(IsSearch)
                return arrayList.size();
            else{
                if(ViewMore || arrayList.size() < 5)
                    return arrayList.size();
                else
                    return 5;
            }
        return 0;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView TenBaiHat, TenCaSi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_song_search);
            TenBaiHat = itemView.findViewById(R.id.txt_song_search);
            TenCaSi = itemView.findViewById(R.id.txt_song_casi_search);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayNhacActivity.class);
                    if (!IsSearch) {
                        intent.putExtra("mangbaihat", arrayList);
                        intent.putExtra("position", getPosition());
                        context.startActivity(intent);
                    }
                    else{
                        ArrayList<BaiHat> BaiHatSearch = new ArrayList<>();
                        BaiHatSearch.add(arrayList.get(getPosition()));
                        intent.putExtra("mangbaihat", arrayList);
                        intent.putExtra("position", 0);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public boolean isViewMore() {
        return ViewMore;
    }

    public void setViewMore(boolean viewMore) {
        ViewMore = viewMore;
        notifyDataSetChanged();
    }

}
