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
import com.example.doanmp3.Activity.PlayNhacActivity;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>{

    Context context;
    ArrayList<BaiHat> arrayList;
    ArrayList<BaiHat> allarraylist;

    public SongAdapter(Context context, ArrayList<BaiHat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        GetData();
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
        holder.txtCaSi.setText(baiHat.getTenAllCaSi());
        Picasso.with(context).load(baiHat.getHinhBaiHat().toString()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayNhacActivity.class);
                    GetData();
                    intent.putExtra("mangbaihat", arrayList);
                    intent.putExtra("position", getPosition());
                    DanhSachBaiHatActivity.category = "Playlist";
                    DanhSachBaiHatActivity.TenCategoty="Hôm nay nghe gì?";
                    context.startActivity(intent);
                }
            });
        }
    }

    private void GetData() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetAllSong();
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                allarraylist = (ArrayList<BaiHat>) response.body();
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    public ArrayList<BaiHat> getallarraylist(){
        if(allarraylist.size() > 0)
            return  allarraylist;

        return  null;
    }
}
