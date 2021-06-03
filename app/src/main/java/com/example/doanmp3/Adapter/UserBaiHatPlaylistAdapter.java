package com.example.doanmp3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.doanmp3.Activity.PlayNhacActivity;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBaiHatPlaylistAdapter extends RecyclerView.Adapter<UserBaiHatPlaylistAdapter.ViewHolder>{

    Context context;
    ArrayList<BaiHat> arrayList;
    ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    String IdPlaylist = "";



    public UserBaiHatPlaylistAdapter(Context context, ArrayList<BaiHat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_baihat_user_playlist,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserBaiHatPlaylistAdapter.ViewHolder holder, int position) {
        BaiHat baiHat = arrayList.get(position);
        if (baiHat == null)
            return;

        viewBinderHelper.bind(holder.swipeRevealLayout, baiHat.getIdBaiHat());
        Picasso.with(context).load(baiHat.getHinhBaiHat()).into(holder.imgBaiHat);
        holder.txtBaiHat.setText(baiHat.getTenBaiHat());
        holder.txtCaSi.setText(baiHat.getTenAllCaSi());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataService dataService = APIService.getUserService();
                Call<String> callback = dataService.DeleteBaiHatPlaylist(IdPlaylist, baiHat.getIdBaiHat());
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = (String) response.body();
                        Log.e("BBB", result);
                        if(result.equals("Thanh Cong")){
                            arrayList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

        holder.btnBaiHat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayNhacActivity.class);
                intent.putExtra("mangbaihat", arrayList);
                intent.putExtra("position", position);
                PlayNhacActivity.random = true;
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(arrayList != null)
            return arrayList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout btnBaiHat;
        SwipeRevealLayout swipeRevealLayout;
        MaterialButton btnDelete;
        TextView txtBaiHat, txtCaSi;
        ImageView imgBaiHat;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            btnBaiHat = itemView.findViewById(R.id.baihat_user_playlist);
            swipeRevealLayout = itemView.findViewById(R.id.dong_baihat_playlist);
            btnDelete = itemView.findViewById(R.id.btn_delete_baihat_playlist);
            txtBaiHat = itemView.findViewById(R.id.txt_song_userplaylist);
            txtCaSi = itemView.findViewById(R.id.txt_song_casi_userplaylist);
            imgBaiHat = itemView.findViewById(R.id.img_song_userplaylist);
        }
    }
    public String getIdPlaylist() {
        return IdPlaylist;
    }

    public void setIdPlaylist(String idPlaylist) {
        IdPlaylist = idPlaylist;
    }
}
