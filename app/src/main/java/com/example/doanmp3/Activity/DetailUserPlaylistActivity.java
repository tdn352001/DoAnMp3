package com.example.doanmp3.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmp3.Adapter.UserBaiHatPlaylistAdapter;
import com.example.doanmp3.Fragment.UserFragment.UserPlaylistFragment;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.User;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUserPlaylistActivity extends AppCompatActivity {

    RoundedImageView imgPlaylist;
    Toolbar toolbar;
    MaterialButton btnPlayAll;
    RelativeLayout btnAddBaiHat;
    RecyclerView recyclerView;
    static RelativeLayout txtNoInf;
    String IdPlaylist;
    String TenPlaylist;
    public static ArrayList<BaiHat> arrayList;
    @SuppressLint("StaticFieldLeak")
    public static UserBaiHatPlaylistAdapter adapter;
    public static User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_playlist);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);

        AnhXa();
        GetIntent();
        SetupToolBar();
        GetBaiHatPlaylist();
        EventClick();
    }

    private void AnhXa() {
        imgPlaylist = findViewById(R.id.img_tieudedanhsach_userplaylist);
        toolbar = findViewById(R.id.toolbar_dsbh_userplaylist);
        btnAddBaiHat = findViewById(R.id.relative_btn_add_baihat_userplaylist);
        btnPlayAll = findViewById(R.id.btn_action_userplaylist);
        recyclerView = findViewById(R.id.rv_dsbh_userplaylist);
        txtNoInf = findViewById(R.id.txt_no_info_user_playlist);
    }

    private void SetupToolBar() {
        toolbar.setTitle(TenPlaylist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }


    private void GetIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("idplaylist")) {
            IdPlaylist = intent.getStringExtra("idplaylist");
            TenPlaylist = intent.getStringExtra("tenplaylist");
        }
        btnAddBaiHat.setClickable(false);
    }

    private void GetBaiHatPlaylist() {
        arrayList = null;
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetUserBaiHatPlaylist(user.getIdUser(), IdPlaylist);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                arrayList = (ArrayList<BaiHat>) response.body();

                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                } else if (arrayList.size() > 0)
                    Glide.with(DetailUserPlaylistActivity.this).load(arrayList.get(0).getHinhBaiHat()).error(R.drawable.song).into(imgPlaylist);
                CheckArrayListEmpty();
                setRV();
                btnAddBaiHat.setClickable(true);
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                btnAddBaiHat.setClickable(true);
            }
        });
    }

    private void EventClick() {
        btnAddBaiHat.setOnClickListener(v -> {
            Intent intent = new Intent(DetailUserPlaylistActivity.this, AddBaiHatActivity.class);
            intent.putExtra("idplaylist", IdPlaylist);
            intent.putExtra("tenplaylist", TenPlaylist);
            intent.putExtra("mangbaihat", arrayList);
            startActivity(intent);
        });
        btnPlayAll.setOnClickListener(v -> {
            if (arrayList != null) {
                if (arrayList.size() > 0) {
                    Intent intentt = new Intent(DetailUserPlaylistActivity.this, PlayNhacActivity.class);
                    Random rd = new Random();
                    DanhSachBaiHatActivity.category = "Playlist";
                    DanhSachBaiHatActivity.TenCategoty = TenPlaylist;
                    intentt.putExtra("mangbaihat", arrayList);
                    intentt.putExtra("position", rd.nextInt(arrayList.size()));
                    startActivity(intentt);
                }
            }
        });
    }

    private void setRV() {
        adapter = new UserBaiHatPlaylistAdapter(DetailUserPlaylistActivity.this, arrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailUserPlaylistActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(adapter);
        LayoutAnimationController animlayout = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_left_to_right);
        recyclerView.setLayoutAnimation(animlayout);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public static void UpdateArraylist(ArrayList<BaiHat> baiHats) {
        if (baiHats != null) {
            arrayList = baiHats;
            CheckArrayListEmpty();
            adapter.UpdateArraylist(arrayList);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete_user_playlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_playlist) {
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(DetailUserPlaylistActivity.this);
            dialog.setBackground(getResources().getDrawable(R.drawable.custom_diaglog_background));
            dialog.setTitle("Xóa Playlist");
            dialog.setIcon(R.drawable.ic_warning);
            dialog.setMessage("Bạn Chắc Muốn Xóa Playlist?");
            dialog.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DeletePlaylist();
                    dialog.dismiss();
                }
            });
            dialog.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        if (item.getItemId() == R.id.change_name) {
            OpenCreateDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void DeletePlaylist() {
        ProgressDialog progressDialog = ProgressDialog.show(this, "Đang Thực Hiện", "Vui Lòng Chờ");
        DataService dataService = APIService.getUserService();
        Call<String> callback = dataService.DeleteUserPlaylist(MainActivity.user.getIdUser(), IdPlaylist);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                if (result.equals("S")) {
                    UserPlaylistFragment.RemovePlaylist(IdPlaylist);
                    Toast.makeText(DetailUserPlaylistActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(DetailUserPlaylistActivity.this, "Lỗi Kết Nối! Vui Lòng Thử Lại Sau.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void OpenCreateDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_name_playlist);
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

        TextView title;

        title = dialog.findViewById(R.id.txt_title_themplaylist);
        edtTenPlaylist = dialog.findViewById(R.id.edt_add_playlist);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnConfirm = dialog.findViewById(R.id.btnAdd);


        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            String tenplaylist = edtTenPlaylist.getText().toString();
            if (tenplaylist.equals(""))
                edtTenPlaylist.setError("Tên Playlist Trống");
            else {
                if (UserPlaylistFragment.userPlaylist != null) {
                    if (!UserPlaylistFragment.CheckUserPlaylistExist(tenplaylist)) {
                        ProgressDialog progressDialog = ProgressDialog.show(this, "Đang Cập  Nhật", "Loading...!", false, false);
                        DataService dataService = APIService.getUserService();
                        Call<String> callback = dataService.ChangeName(IdPlaylist, tenplaylist);
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                UserPlaylistFragment.ChangeNamePlaylist(IdPlaylist, tenplaylist);
                                Intent intent = new Intent(DetailUserPlaylistActivity.this, DetailUserPlaylistActivity.class);
                                intent.putExtra("idplaylist", IdPlaylist);
                                intent.putExtra("tenplaylist", tenplaylist);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                progressDialog.dismiss();
                                dialog.dismiss();
                                Toast.makeText(DetailUserPlaylistActivity.this, "Đã Cập Nhật Playlist", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(DetailUserPlaylistActivity.this, "Lỗi Kết Nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        edtTenPlaylist.setError("Playlist Đã Tồn tại");
                        Toast.makeText(this, "Playlist Đã Tồn Tại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialog.show();
    }

    public static void CheckArrayListEmpty() {
        if (arrayList != null) {
            if (arrayList.size() > 0) {
                txtNoInf.setVisibility(View.GONE);
                return;
            }
        }
        txtNoInf.setVisibility(View.VISIBLE);
    }

    @Override
    public void finish() {
        super.finish();
        adapter = null;
        arrayList = null;
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}