package com.example.doanmp3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.DetailUserPlaylistActivity;
import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPlaylistAdapter extends RecyclerView.Adapter<UserPlaylistAdapter.ViewHolder> {

    ArrayList<Playlist> arrayList;
    Context context;
    boolean addbaihat;
    String IdBaiHat;
    boolean IsResponse;



    public UserPlaylistAdapter(ArrayList<Playlist> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        addbaihat = false;
        IdBaiHat = "";
        IsResponse = false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_user_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPlaylistAdapter.ViewHolder holder, int position) {
        Playlist playlist = arrayList.get(position);
        holder.txtTenPlaylist.setText(playlist.getTen());
    }

    @Override
    public int getItemCount() {
        if (arrayList != null)
            return arrayList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTenPlaylist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenPlaylist = itemView.findViewById(R.id.txt_ten_playlist_user);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (addbaihat) {
                                DataService dataService = APIService.getUserService();
                                Call<String> callback = dataService.ThemBaiHatPlaylist(arrayList.get(getPosition()).getIdPlaylist(), IdBaiHat);
                                callback.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String result = (String) response.body();
                                        IsResponse = true;
                                        if(result.equals("Thanh Cong"))
                                            Toast.makeText(context, "Đã Cập Nhật Playlist", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(context, "Bài Hát Đã Được Thêm Trước Đó", Toast.LENGTH_SHORT).show();


                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(context, "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Intent intent = new Intent(context, DetailUserPlaylistActivity.class);
                                intent.putExtra("idplaylist", arrayList.get(getPosition()).getIdPlaylist());
                                intent.putExtra("tenplaylist", arrayList.get(getPosition()).getTen());
                                context.startActivity(intent);
                            }

                        }
                    });
                }
            });
        }
    }

    public boolean isAddbaihat() {
        return addbaihat;
    }

    public void setAddbaihat(boolean addbaihat) {
        this.addbaihat = addbaihat;
    }

    public String getIdBaiHat() {
        return IdBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        IdBaiHat = idBaiHat;
    }

    public boolean isResponse() {
        return IsResponse;
    }

    public void setResponse(boolean response) {
        IsResponse = response;
    }


}
