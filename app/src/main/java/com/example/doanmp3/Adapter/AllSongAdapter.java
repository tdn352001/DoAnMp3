package com.example.doanmp3.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.MainActivity;
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

public class AllSongAdapter extends RecyclerView.Adapter<AllSongAdapter.ViewHolder> {
    Context context;
    ArrayList<BaiHat> arrayList;

    public AllSongAdapter(Context context, ArrayList<BaiHat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_all_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = arrayList.get(position);
        Picasso.with(context).load(baiHat.getHinhBaiHat()).into(holder.imageView);
        holder.TenCaSi.setText(baiHat.getTenAllCaSi());
        holder.TenBaiHat.setText(baiHat.getTenBaiHat());
    }

    @Override
    public int getItemCount() {
        if (arrayList.size() > 0)
            return arrayList.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView TenBaiHat, TenCaSi;
        MaterialButton btnOptions;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Ánh Xạ
            imageView = itemView.findViewById(R.id.img_song_all);
            TenBaiHat = itemView.findViewById(R.id.txt_song_all);
            TenCaSi = itemView.findViewById(R.id.txt_song_casi_all);
            btnOptions = itemView.findViewById(R.id.btn_options_baihat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayNhacActivity.class);
                    intent.putExtra("mangbaihat", arrayList);
                    intent.putExtra("position", getPosition());
                    PlayNhacActivity.random = true;
                    context.startActivity(intent);
                }
            });

            btnOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu =new PopupMenu(context, btnOptions);
                    setupPopupMenu(getPosition(), popupMenu);
                    if (popupMenu != null) {
                        popupMenu.show();
                    }
                }
            });
        }

        @SuppressLint("NewApi")
        @RequiresApi(api = Build.VERSION_CODES.M)
        private void setupPopupMenu(int postion, PopupMenu popupMenu) {
            popupMenu.getMenuInflater().inflate(R.menu.menu_options_baihat, popupMenu.getMenu());
            popupMenu.setGravity(Gravity.RIGHT);
            popupMenu.setForceShowIcon(true);

            if (MainActivity.checkLiked(arrayList.get(postion).getIdBaiHat()))
                popupMenu.getMenu().getItem(0).setTitle("Bỏ Thích");
            else
                popupMenu.getMenu().getItem(0).setTitle("Thích");
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.baihat_like) {
                        if (MainActivity.checkLiked(arrayList.get(postion).getIdBaiHat())) {
                            BoThich(MainActivity.user.getIdUser(), arrayList.get(postion).getIdBaiHat(), postion);
                        } else
                            Thich(MainActivity.user.getIdUser(), arrayList.get(postion).getIdBaiHat(), postion);
                    } else {
                        Toast.makeText(context, "Đã" + item.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

        }
    }

    public void setArrayList(ArrayList<BaiHat> baiHats) {
        arrayList = baiHats;
        notifyDataSetChanged();
    }


    public void BoThich(String iduser, String idbaihat, int position) {
        Log.d("BBB", "Bo Thich");
        DataService dataService = APIService.getUserService();
        Call<String> callback = dataService.BoThichBaiHat(idbaihat, iduser);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = (String) response.body();
                Log.d("BBB", result);
                if (result.equals("Thanh Cong")) {
                    Toast.makeText(context, "Đã Bỏ Thích", Toast.LENGTH_SHORT).show();
                    MainActivity.baihatyeuthichList.remove(arrayList.get(position));
                    notifyDataSetChanged();
                } else
                    Toast.makeText(context, "Lỗi Hệ Thống", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void Thich(String iduser, String idbaihat, int position) {
        Log.d("BBB", "Thich");
        DataService dataService = APIService.getUserService();
        Call<String> callback = dataService.YeuThichBaiHat(idbaihat, iduser);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = (String) response.body();
                notifyDataSetChanged();
                Log.d("BBB", result);
                if (result.equals("Thanh Cong")) {
                    if (!MainActivity.baihatyeuthichList.contains(arrayList.get(position)))
                        MainActivity.baihatyeuthichList.add(arrayList.get(position));
                    Toast.makeText(context, "Đã Thích", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "Lỗi Hệ Thống", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Lỗi Mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
