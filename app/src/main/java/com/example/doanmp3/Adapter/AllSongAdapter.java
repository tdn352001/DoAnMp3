package com.example.doanmp3.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.DanhSachBaiHatActivity;
import com.example.doanmp3.Activity.MainActivity;
import com.example.doanmp3.Activity.PlayNhacActivity;
import com.example.doanmp3.Fragment.UserFragment.UserBaiHatFragment;
import com.example.doanmp3.Fragment.UserFragment.UserPlaylistFragment;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSongAdapter extends RecyclerView.Adapter<AllSongAdapter.ViewHolder> {
    Context context;
    ArrayList<BaiHat> arrayList;
    boolean isUserBaiHat;


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
        Picasso.with(context).load(baiHat.getHinhBaiHat()).error(R.drawable.song).into(holder.imageView);
        holder.TenCaSi.setText(baiHat.getTenAllCaSi());
        holder.TenBaiHat.setText(baiHat.getTenBaiHat());

    }

    @Override
    public int getItemCount() {
        if (arrayList != null)
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
                    if(isUserBaiHat){
                        DanhSachBaiHatActivity.category = "Playlist";
                        DanhSachBaiHatActivity.TenCategoty = "Bài Hát Yêu Thích";
                    }
                    context.startActivity(intent);
                }
            });

            btnOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, btnOptions);
                    setupPopupMenu(getPosition(), popupMenu);
                    popupMenu.show();
                }
            });
        }

        @SuppressLint("NewApi")
        @RequiresApi(api = Build.VERSION_CODES.M)
        private void setupPopupMenu(int postion, PopupMenu popupMenu) {
            popupMenu.getMenuInflater().inflate(R.menu.menu_options_baihat, popupMenu.getMenu());
            popupMenu.setGravity(Gravity.RIGHT);
            popupMenu.setForceShowIcon(true);

            if (UserBaiHatFragment.checkLiked(arrayList.get(postion).getIdBaiHat())) {
                popupMenu.getMenu().getItem(0).setTitle("Bỏ Thích");
                popupMenu.getMenu().getItem(0).setIcon(R.drawable.ic_love);
            } else {
                popupMenu.getMenu().getItem(0).setTitle("Thích");
                popupMenu.getMenu().getItem(0).setIcon(R.drawable.ic_hate);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.baihat_like) {
                        if (UserBaiHatFragment.checkLiked(arrayList.get(postion).getIdBaiHat())) {
                            BoThich(MainActivity.user.getIdUser(), arrayList.get(postion).getIdBaiHat(), postion);
                        } else
                            Thich(MainActivity.user.getIdUser(), arrayList.get(postion).getIdBaiHat(), postion);
                    } else {
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                        setupBottomSheetMenu(bottomSheetDialog, getPosition());
                    }
                    return true;
                }
            });

        }

        private void setupBottomSheetMenu(BottomSheetDialog dialog, int position){
            dialog.setContentView(R.layout.dialog_add_baihat_playlist);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            RoundedImageView imgBaiHat;
            TextView txtBaiHat, txtCaSi;
            MaterialButton btnAddPlaylist;
            RecyclerView recyclerView;

            // Ánh Xạ
            imgBaiHat = dialog.findViewById(R.id.img_baihat_bottomsheet_add);
            txtBaiHat = dialog.findViewById(R.id.txt_tenbaihat_bottomsheet_add);
            txtCaSi = dialog.findViewById(R.id.txt_tencasi_bottomsheet_add);
            btnAddPlaylist = dialog.findViewById(R.id.add_playlist_bottomsheet_add);
            recyclerView = dialog.findViewById(R.id.rv_bottomsheet_add);

            // Set Ảnh cộng tên
            Picasso.with(context).load(arrayList.get(position).getHinhBaiHat()).into(imgBaiHat);
            txtBaiHat.setText(arrayList.get(position).getTenBaiHat());
            txtCaSi.setText(arrayList.get(position).getTenAllCaSi());

            // Set RV
            ArrayList<Playlist> playlists = MainActivity.userPlaylist;
            UserPlaylistAdapter adapter = new UserPlaylistAdapter(playlists, context);
            adapter.setAddbaihat(true);
            adapter.setIdBaiHat(arrayList.get(position).getIdBaiHat());
            recyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);

            // bat su kien click tren recycleview de tat dialog

            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if(adapter.isResponse()){
                        dialog.dismiss();
                    }
                    Log.e("BBB", "vandem");
                    handler.postDelayed(this, 200);
                }
            };
            handler.postDelayed(runnable, 200);

            // taoplaylist moi
            btnAddPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenCreateDialog(position);
                    dialog.dismiss();
                }
            });

            // show dialog
            dialog.show();
        }

        private void OpenCreateDialog(int position) {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_add_playlist);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Window window = dialog.getWindow();

            if (window == null)
                return;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
            dialog.setCancelable(true);

            TextInputEditText edtTenPlaylist;
            MaterialButton btnConfirm, btnCancel;

            edtTenPlaylist = dialog.findViewById(R.id.edt_add_playlist);
            btnCancel = dialog.findViewById(R.id.btnCancel);
            btnConfirm = dialog.findViewById(R.id.btnAdd);


            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tenplaylist = edtTenPlaylist.getText().toString();
                    if (tenplaylist.equals(""))
                        edtTenPlaylist.setError("Tên Playlist Trống");
                    else {
                        int i = 0;
                        if (MainActivity.userPlaylist != null) {
                            for (i = 0; i < MainActivity.userPlaylist.size(); i++) {
                                if (MainActivity.userPlaylist.get(i).getTen().equals(tenplaylist))
                                    return;
                            }
                            if(i >= MainActivity.userPlaylist.size() || i ==0){
                                ProgressDialog progressDialog;
                                progressDialog = ProgressDialog.show(context,"Đang Tạo Playlist", "Loading...!", false, false);
                                DataService dataService = APIService.getUserService();
                                Call<String> callback = dataService.TaoPlaylist(MainActivity.user.getIdUser(), tenplaylist);
                                callback.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String Idplaylist = (String) response.body();
                                        if(Idplaylist.equals("That Bai"))
                                            Toast.makeText(context, "Lỗi Hệ Thống", Toast.LENGTH_SHORT).show();
                                        else
                                        {
                                            // Thêm Playlist sau khi tạo
                                            Playlist playlist = new Playlist();
                                            playlist.setIdPlaylist(Idplaylist);
                                            playlist.setTen(tenplaylist);
                                            playlist.setHinhAnh("https://tiendung352001.000webhostapp.com/Client/image/ic_user_playlist.png");
                                            UserPlaylistFragment.userPlaylist.add(playlist);

                                            // Thêm Bài Hát Vào Playlist Mới tạo
                                            Call<String> callBack = dataService.ThemBaiHatPlaylist(Idplaylist, arrayList.get(position).getIdBaiHat());
                                            callBack.enqueue(new Callback<String>() {
                                                @Override
                                                public void onResponse(Call<String> call, Response<String> response) {
                                                    String result = (String) response.body();
                                                    if(result.equals("Thanh Cong"))
                                                        Toast.makeText(context, "Đã Cập Nhật Playlist", Toast.LENGTH_SHORT).show();
                                                    else
                                                        Toast.makeText(context, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(Call<String> call, Throwable t) {

                                                }
                                            });
                                            UserPlaylistFragment.adapter.notifyDataSetChanged();
                                            dialog.dismiss();
                                        }
                                        progressDialog.dismiss();

                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                edtTenPlaylist.setError("Playlist Đã Tồn tại");
                            }
                        }
                    }
                }
            });

            dialog.show();
        }


    }

    public void setArrayList(ArrayList<BaiHat> baiHats) {
        arrayList = baiHats;
        notifyDataSetChanged();
    }


    public void BoThich(String iduser, String idbaihat, int position) {
        DataService dataService = APIService.getUserService();
        Call<String> callback = dataService.BoThichBaiHat(idbaihat, iduser);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = (String) response.body();
                if (result.equals("Thanh Cong")) {
                    Toast.makeText(context, "Đã Bỏ Thích", Toast.LENGTH_SHORT).show();
                    Log.d("BBB", UserBaiHatFragment.arrayList.size() + "");
                    UserBaiHatFragment.BoThichBaiHat(arrayList.get(position).getIdBaiHat());
                    if (UserBaiHatFragment.arrayList.size() <= 0)
                        UserBaiHatFragment.textView.setVisibility(View.VISIBLE);

                } else
                    Toast.makeText(context, "Lỗi Hệ Thống", Toast.LENGTH_SHORT).show();
                UserBaiHatFragment.adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void Thich(String iduser, String idbaihat, int position) {
        DataService dataService = APIService.getUserService();
        Call<String> callback = dataService.YeuThichBaiHat(idbaihat, iduser);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = (String) response.body();
                if (result.equals("Thanh Cong")) {
                    if (!UserBaiHatFragment.arrayList.contains(arrayList.get(position)))
                        UserBaiHatFragment.arrayList.add(arrayList.get(position));
                    if(UserBaiHatFragment.adapter.getItemCount() >= 0)
                        UserBaiHatFragment.textView.setVisibility(View.GONE);

                    UserBaiHatFragment.adapter.notifyDataSetChanged();
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

    public void setUserBaiHat(boolean userBaiHat) {
        isUserBaiHat = userBaiHat;
    }


}
