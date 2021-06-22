
package com.example.doanmp3.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.doanmp3.Adapter.ViewPagerAdapter;
import com.example.doanmp3.Fragment.UserFragment.Added_AddFragment;
import com.example.doanmp3.Fragment.UserFragment.Love_AddFragment;
import com.example.doanmp3.Fragment.UserFragment.Online_AddFragment;
import com.example.doanmp3.Fragment.UserFragment.Recent_AddFragment;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBaiHatActivity extends AppCompatActivity {

    Toolbar toolbar;
    public static SearchView searchView;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    public static ArrayList<BaiHat> baiHatsAdded;
    private ArrayList<BaiHat> arrayList;

    //Fragment in View Pager;
    static Added_AddFragment addedFragment;
    static Love_AddFragment loveFragment;
    static Online_AddFragment onlineFragment;
    static Recent_AddFragment recentFragment;

    ProgressDialog progressDialog;
    //DetailPlaylist
    String IdPlaylist, TenPlaylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bai_hat);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);

        AnhXa();
        GetInfoPlaylist();
        SetToolBar();
        SetViewPager();
        SetUpSearchView();
    }

    private void GetInfoPlaylist() {
        Intent intent = getIntent();
        IdPlaylist = intent.getStringExtra("idplaylist");
        TenPlaylist = intent.getStringExtra("tenplaylist");
        if (intent.hasExtra("mangbaihat")) {
            baiHatsAdded = intent.getParcelableArrayListExtra("mangbaihat");
            arrayList = new ArrayList<>();
            arrayList.addAll(baiHatsAdded);
        }
    }


    private void AnhXa() {
        tabLayout = findViewById(R.id.tablayout_addbaihat);
        toolbar = findViewById(R.id.toolbar_addbaihat);
        viewPager = findViewById(R.id.viewpager_addbaihat);
        searchView = findViewById(R.id.search_view_addbaihat);
    }

    private void SetToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(TenPlaylist);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChange()) {
                    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(AddBaiHatActivity.this);
                    dialog.setBackground(getResources().getDrawable(R.drawable.custom_diaglog_background));
                    dialog.setTitle("Thoát");
                    dialog.setIcon(R.drawable.ic_warning);
                    dialog.setMessage("Bạn Có Muốn Lưu Kết Quả?");
                    dialog.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SaveChange();
                        }
                    });
                    dialog.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Added_AddFragment.arrayList = null;
                            AddBaiHatActivity.super.onBackPressed();
                        }
                    });
                    dialog.show();

                } else
                    finish();
            }
        });
    }


    private void SetViewPager() {
        // List Fragment
        addedFragment = new Added_AddFragment();
        loveFragment = new Love_AddFragment();
        onlineFragment = new Online_AddFragment();
        recentFragment = new Recent_AddFragment();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(addedFragment);
        fragmentList.add(onlineFragment);
        fragmentList.add(loveFragment);
        fragmentList.add(recentFragment);

        // List Title
        ArrayList<String> titles = new ArrayList<>();
        titles.add("Đã Thêm");
        titles.add("Online");
        titles.add("Yêu Thích");
        titles.add("Gần Đây");

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.setList(fragmentList);
        viewPagerAdapter.setTitle(titles);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);


    }

    // Tìm Kiếm
    private void SetUpSearchView() {
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Khi submitText
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals("")) {
                    addedFragment.adapter.getFilter().filter(query);
                    SearchOnline(query);
                    loveFragment.adapter.getFilter().filter(query);
                    recentFragment.adapter.getFilter().filter(query);

                    if (addedFragment.adapter.getItemCount() == 0) {
                        addedFragment.textView.setVisibility(View.VISIBLE);
                    } else addedFragment.textView.setVisibility(View.GONE);

                    if (loveFragment.adapter.getItemCount() == 0) {
                        loveFragment.textView.setVisibility(View.VISIBLE);
                    } else loveFragment.textView.setVisibility(View.GONE);

                    if (recentFragment.adapter.getItemCount() == 0) {
                        recentFragment.textView.setVisibility(View.VISIBLE);
                    } else recentFragment.textView.setVisibility(View.GONE);


                }
                return true;
            }

            // Khi Text Thay đổi
            @Override
            public boolean onQueryTextChange(String newText) {

                addedFragment.adapter.getFilter().filter(newText);
                loveFragment.adapter.getFilter().filter(newText);
                recentFragment.adapter.getFilter().filter(newText);

                // Kiểm tra kết quả lần 1
                if (1 == 1) {
                    if (newText.equals("")) {
                        onlineFragment.SearchArrayList = onlineFragment.arrayList;
                        onlineFragment.SetResultBaiHat();
                    } else
                        SearchOnline(newText);
                    if (addedFragment.adapter.getItemCount() == 0) {
                        addedFragment.textView.setVisibility(View.VISIBLE);
                    } else
                        addedFragment.textView.setVisibility(View.GONE);

                    // Kiểm Tra Kết Quả Bài Hát Yêu Thích
                    if (loveFragment.adapter.getItemCount() == 0) {
                        loveFragment.textView.setVisibility(View.VISIBLE);
                    } else
                        loveFragment.textView.setVisibility(View.GONE);
                    // Kiểm Tra Kết Quả Bài Hát Gần Đây
                    if (recentFragment.adapter.getItemCount() == 0) {
                        recentFragment.textView.setVisibility(View.VISIBLE);
                    } else
                        recentFragment.textView.setVisibility(View.GONE);
                }

                // Kiểm tra kết quả lần 2
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (addedFragment.adapter.getItemCount() == 0) {
                            addedFragment.textView.setVisibility(View.VISIBLE);
                        } else
                            addedFragment.textView.setVisibility(View.GONE);

                        // Kiểm Tra Kết Quả Bài Hát Yêu Thích
                        if (loveFragment.adapter.getItemCount() == 0) {
                            loveFragment.textView.setVisibility(View.VISIBLE);
                        } else
                            loveFragment.textView.setVisibility(View.GONE);
                        // Kiểm Tra Kết Quả Bài Hát Gần Đây
                        if (recentFragment.adapter.getItemCount() == 0) {
                            recentFragment.textView.setVisibility(View.VISIBLE);
                        } else
                            recentFragment.textView.setVisibility(View.GONE);
                    }
                }, 200);


                return true;
            }
        });
    }

    // Thay đổi trạng thái khi click vào item ở các tab123

    public static void ChangeStatus(String IdBaiHat) {
        if (loveFragment.arrayList.size() > 0) {
            for (int i = 0; i < loveFragment.arrayList.size(); i++)
                if (loveFragment.arrayList.get(i).getIdBaiHat().equals(IdBaiHat)) {
                    loveFragment.adapter.notifyItemChanged(i);
                    break;
                }
        }
        if (recentFragment.arrayList.size() > 0) {
            for (int i = 0; i < recentFragment.arrayList.size(); i++)
                if (recentFragment.arrayList.get(i).getIdBaiHat().equals(IdBaiHat)) {
                    recentFragment.adapter.notifyItemChanged(i);
                    break;
                }
        }
        if (onlineFragment.SearchArrayList.size() > 0) {
            for (int i = 0; i < onlineFragment.SearchArrayList.size(); i++)
                if (onlineFragment.SearchArrayList.get(i).getIdBaiHat().equals(IdBaiHat)) {
                    onlineFragment.adapter.notifyItemChanged(i);
                    break;
                }
        }
    }

    // TÌm Kiếm Ở tab Online
    private void SearchOnline(String query) {
        onlineFragment.SearchArrayList = null;
        Call<List<BaiHat>> callback = APIService.getService().GetSearchBaiHat(query);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                onlineFragment.SearchArrayList = (ArrayList<BaiHat>) response.body();
                if (onlineFragment.SearchArrayList == null) {
                    onlineFragment.SearchArrayList = new ArrayList<>();
                }
                onlineFragment.SetResultBaiHat();

            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
            }
        });
    }

    // Btn Lưu Kết Quả Trên Thanh Menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SaveChange();
        return super.onOptionsItemSelected(item);
    }

    // Khi Ấn Nút Quay Lại


    @Override
    public void onBackPressed() {
        if (isChange()) {
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(AddBaiHatActivity.this);
            dialog.setBackground(getResources().getDrawable(R.drawable.custom_diaglog_background));
            dialog.setTitle("Thoát");
            dialog.setIcon(R.drawable.ic_warning);
            dialog.setMessage("Bạn Có Muốn Lưu Kết Quả?");
            dialog.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SaveChange();
                }
            });
            dialog.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Added_AddFragment.arrayList = null;
                    AddBaiHatActivity.super.onBackPressed();
                }
            });
            dialog.show();

        } else
            super.onBackPressed();
    }

    private void SaveChange() {
        progressDialog = ProgressDialog.show(AddBaiHatActivity.this, "Đang Cập Nhật Playlsit", "Vui lòng chờ...!", true, true);
        if (isChange()) {
            DataService dataService = APIService.getUserService();
            Call<String> callUpdate = dataService.UpdateUserPlaylist(IdPlaylist);
            callUpdate.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    for (int i = 0; i < Added_AddFragment.arrayList.size(); i++) {
                        Call<String> callback = dataService.ThemBaiHatPlaylist(IdPlaylist, Added_AddFragment.arrayList.get(i).getIdBaiHat());
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    }
                    progressDialog.dismiss();
                    DetailUserPlaylistActivity.UpdateArraylist(Added_AddFragment.arrayList);
//                    DetailUserPlaylistActivity.arrayList = Added_AddFragment.arrayList;
//                    DetailUserPlaylistActivity.adapter.notifyDataSetChanged();
                    Added_AddFragment.arrayList = null;
                    Toast.makeText(AddBaiHatActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                    finish();

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        } else {
            finish();
            progressDialog.dismiss();
        }
    }

    private boolean isChange() {
        return !arrayList.equals(Added_AddFragment.arrayList);
    }

    @Override
    public void finish() {
        super.finish();
        searchView = null;
        baiHatsAdded = null;
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }

}