package com.example.doanmp3.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import com.example.doanmp3.Service.MusicService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;

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
        Glide.with(context).load(baiHat.getHinhBaiHat()).error(R.drawable.song).into(holder.imageView);
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

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, PlayNhacActivity.class);
                intent.putExtra("mangbaihat", arrayList);
                intent.putExtra("position", getPosition());
                if (isUserBaiHat) {
                    DanhSachBaiHatActivity.category = "Playlist";
                    DanhSachBaiHatActivity.TenCategoty = "Bài Hát Yêu Thích";
                }
                context.startActivity(intent);
            });

            btnOptions.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, btnOptions);
                setupPopupMenu(getPosition(), popupMenu);
                popupMenu.show();
            });
        }

        @SuppressLint({"NewApi", "NonConstantResourceId"})
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
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.baihat_like:
                        if (UserBaiHatFragment.checkLiked(arrayList.get(postion).getIdBaiHat())) {
                            BoThich(MainActivity.user.getIdUser(), arrayList.get(postion).getIdBaiHat(), postion);
                        } else
                            Thich(MainActivity.user.getIdUser(), arrayList.get(postion).getIdBaiHat(), postion);
                        break;
                    case R.id.baihat_add:
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                        setupBottomSheetMenu(bottomSheetDialog, getPosition());
                        break;
                    case R.id.add_to_queue:
                        StartService(postion);
                        popupMenu.dismiss();
                        break;
                    case R.id.download:
                        popupMenu.dismiss();
                        Download(postion);
                        break;
                }

                return true;
            });
        }

        private void setupBottomSheetMenu(BottomSheetDialog dialog, int position) {
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
            Glide.with(context).load(arrayList.get(position).getHinhBaiHat()).error(R.drawable.song).into(imgBaiHat);
            if (txtBaiHat != null) {
                txtBaiHat.setText(arrayList.get(position).getTenBaiHat());
            }
            if (txtCaSi != null) {
                txtCaSi.setText(arrayList.get(position).getTenAllCaSi());
            }

            // Set RV
            ArrayList<Playlist> playlists = MainActivity.userPlaylist;
            UserPlaylistAdapter adapter = new UserPlaylistAdapter(playlists, context);
            adapter.setAddbaihat(true);
            adapter.setIdBaiHat(arrayList.get(position).getIdBaiHat());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            if (recyclerView != null) {
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(linearLayoutManager);
            }

            // bat su kien click tren recycleview de tat dialog

            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (adapter.isResponse()) {
                        dialog.dismiss();
                        handler.removeCallbacks(this);
                    }
                    handler.postDelayed(this, 100);
                }
            };
            handler.postDelayed(runnable, 100);

            // taoplaylist moi
            assert btnAddPlaylist != null;
            btnAddPlaylist.setOnClickListener(v -> {
                OpenCreateDialog(position);
                dialog.dismiss();
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


            btnCancel.setOnClickListener(v -> dialog.dismiss());

            btnConfirm.setOnClickListener(v -> {
                String tenplaylist = edtTenPlaylist.getText().toString().trim();
                if (tenplaylist.equals(""))
                    edtTenPlaylist.setError("Tên Playlist Trống");
                else {
                    int i = 0;
                    if (MainActivity.userPlaylist != null) {
                        for (i = 0; i < MainActivity.userPlaylist.size(); i++) {
                            if (MainActivity.userPlaylist.get(i).getTen().equals(tenplaylist))
                                break;
                        }
                        if (i >= MainActivity.userPlaylist.size() || i == 0) {
                            ProgressDialog progressDialog;
                            progressDialog = ProgressDialog.show(context, "Đang Tạo Playlist", "Loading...!", false, false);
                            DataService dataService = APIService.getUserService();
                            Call<String> callback = dataService.TaoPlaylist(MainActivity.user.getIdUser(), tenplaylist);
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String Idplaylist = (String) response.body();
                                    if (Idplaylist.equals("That Bai"))
                                        Toast.makeText(context, "Lỗi Hệ Thống", Toast.LENGTH_SHORT).show();
                                    else {
                                        // Thêm Playlist sau khi tạo
                                        Playlist playlist = new Playlist();
                                        playlist.setIdPlaylist(Idplaylist);
                                        playlist.setTen(tenplaylist);
                                        playlist.setHinhAnh("https://tiendung352001.000webhostapp.com/Client/image/ic_user_playlist.png");
                                        UserPlaylistFragment.userPlaylist.add(playlist);
                                        UserPlaylistFragment.CheckArrayListEmpty();
                                        // Thêm Bài Hát Vào Playlist Mới tạo
                                        Call<String> callBack = dataService.ThemBaiHatPlaylist(Idplaylist, arrayList.get(position).getIdBaiHat());
                                        callBack.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                String result = (String) response.body();
                                                if (result != null) {
                                                    if (result.equals("Thanh Cong"))
                                                        Toast.makeText(context, "Đã Cập Nhật Playlist", Toast.LENGTH_SHORT).show();
                                                    else
                                                        Toast.makeText(context, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();
                                                }
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
                        } else {
                            edtTenPlaylist.setError("Playlist Đã Tồn tại");
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

    private void StartService(int Pos) {
        Intent intent = new Intent(context, MusicService.class);
        if (MusicService.arrayList == null) {
            ArrayList<BaiHat> baiHats = new ArrayList<>();
            baiHats.add(arrayList.get(Pos));
            intent.putExtra("mangbaihat", baiHats);
            intent.putExtra("audio", false);
            intent.putExtra("pos", 0);
            intent.putExtra("recent", false);
            DanhSachBaiHatActivity.category ="Playlist";
            DanhSachBaiHatActivity.TenCategoty="Ngẫu Nhiên";
            context.startService(intent);
            Toast.makeText(context, "Đã Thêm", Toast.LENGTH_SHORT).show();
        } else {

            MusicService.AddtoPlaylist(context, arrayList.get(Pos));
        }
    }

    private void Download(int Pos) {
        try {
            String link = arrayList.get(Pos).getLinkBaiHat();
            String title = URLUtil.guessFileName(link, null, null);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));
            request.setDescription("Tải xuống " + title);
            request.setTitle(title);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, title);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);
            Toast.makeText(context, "Đang Tải Xuống", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Tải Xuống Thất Bại", Toast.LENGTH_SHORT).show();
        }

    }


    public void BoThich(String iduser, String idbaihat, int position) {
        DataService dataService = APIService.getUserService();
        Call<String> callback = dataService.BoThichBaiHat(idbaihat, iduser);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = (String) response.body();
                if (result != null) {
                    if (result.equals("Thanh Cong")) {
                        Toast.makeText(context, "Đã Bỏ Thích", Toast.LENGTH_SHORT).show();
                        UserBaiHatFragment.BoThichBaiHat(arrayList.get(position).getIdBaiHat());
                        if (UserBaiHatFragment.arrayList.size() <= 0)
                            UserBaiHatFragment.textView.setVisibility(View.VISIBLE);

                    } else
                        Toast.makeText(context, "Lỗi Hệ Thống", Toast.LENGTH_SHORT).show();
                }
                UserBaiHatFragment.adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {

            }
        });
    }

    public void Thich(String iduser, String idbaihat, int position) {
        DataService dataService = APIService.getUserService();
        Call<String> callback = dataService.YeuThichBaiHat(idbaihat, iduser);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                String result = (String) response.body();
                if (result != null) {
                    if (result.equals("Thanh Cong")) {
                        if (!UserBaiHatFragment.arrayList.contains(arrayList.get(position)))
                            UserBaiHatFragment.arrayList.add(arrayList.get(position));
                        if (UserBaiHatFragment.adapter.getItemCount() >= 0)
                            UserBaiHatFragment.textView.setVisibility(View.GONE);

                        UserBaiHatFragment.adapter.notifyDataSetChanged();
                        Toast.makeText(context, "Đã Thích", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(context, "Lỗi Hệ Thống", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                Toast.makeText(context, "Lỗi Mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUserBaiHat(boolean userBaiHat) {
        isUserBaiHat = userBaiHat;
    }
}

