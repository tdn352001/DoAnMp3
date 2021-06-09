package com.example.doanmp3.Fragment.SearchFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Activity.MainActivity;
import com.example.doanmp3.Adapter.AllCategoryAdapter;
import com.example.doanmp3.Adapter.SearchSongAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.ChuDeTheLoai;
import com.example.doanmp3.Model.KeyWord;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentFragment extends Fragment {

    View view;

    //     Phần Tìm Kiếm Gần Đây
    RelativeLayout SearchRecentLayout;
    TextView btnDelete;
    FlowLayout flowLayout;
    public static ArrayList<KeyWord> keyWordArrayList;

    // Bài Hát Gần Đây
    MaterialButton btnViewMore;
    RelativeLayout SongRecentLayout;
    RecyclerView rvBaiHatRecent;
    public static ArrayList<BaiHat> baihatrecents;
    public static SearchSongAdapter searchSongAdapter;
    //ChuDeTheLoai
    RecyclerView rvcategory;
    ArrayList<ChuDeTheLoai> categoryList;
    AllCategoryAdapter categoryAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recent, container, false);
        AnhXa();
        GetResentKeyWord();
        GetRecentSong();
        SetCategoty();
        EventClick();
        return view;
    }


    private void AnhXa() {
        // Phần Tìm Kiếm Gần Đây
        SearchRecentLayout = view.findViewById(R.id.layout_recent_search);
        flowLayout = view.findViewById(R.id.flowlayout_history_search);
        btnDelete = view.findViewById(R.id.btn_delete_history_search);

        // Bài Hát Gần Đây
        rvBaiHatRecent = view.findViewById(R.id.rv_recent_baihat);
        SongRecentLayout = view.findViewById(R.id.layout_recent_song);
        btnViewMore = view.findViewById(R.id.btn_viewmore_baihat_recent);
        //Phần category
        rvcategory = view.findViewById(R.id.rv_category_recent_fragment);
    }

    private void GetResentKeyWord() {
        DataService dataService = APIService.getUserService();
        Call<List<KeyWord>> callback = dataService.GetKeyWordRecent(MainActivity.user.getIdUser());
        callback.enqueue(new Callback<List<KeyWord>>() {
            @Override
            public void onResponse(Call<List<KeyWord>> call, Response<List<KeyWord>> response) {
                keyWordArrayList = (ArrayList<KeyWord>) response.body();
                SetFlowLayout();
            }

            @Override
            public void onFailure(Call<List<KeyWord>> call, Throwable t) {
                Log.e("BBB", "loi");
            }
        });
    }

    private void SetFlowLayout() {

        if (flowLayout == null)
            return;
        flowLayout.removeAllViews();

        if (keyWordArrayList != null) {
            for (int i = 0; i < keyWordArrayList.size(); i++) {

                TextView textView = new TextView(getContext());
                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(35, 35, 0, 0);
                textView.setLayoutParams(params);
                textView.setId(Integer.parseInt(keyWordArrayList.get(i).getIdSearch()));
                textView.setText(keyWordArrayList.get(i).getKeyWord());
                textView.setPadding(15, 5, 15, 5);
                textView.setTextSize(16);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundResource(R.drawable.custom_item_search_recent);
                textView.setTextColor(getResources().getColor(R.color.purple_500));
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                flowLayout.addView(textView);
            }
        } else
            SearchRecentLayout.setVisibility(View.GONE);
    }

    private void GetRecentSong() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatRecent(MainActivity.user.getIdUser());
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                baihatrecents = (ArrayList<BaiHat>) response.body();
                if (baihatrecents == null) {
                    baihatrecents = new ArrayList<>();
                    searchSongAdapter = new SearchSongAdapter(getContext(), baihatrecents, false);
                    SongRecentLayout.setVisibility(View.GONE);
                } else {
                    searchSongAdapter = new SearchSongAdapter(getContext(), baihatrecents, false);
                    Log.e("BBB", baihatrecents.size() + "");
                    searchSongAdapter.notifyDataSetChanged();
                    Log.e("BBB", searchSongAdapter.getItemCount() + "size");
                    rvBaiHatRecent.setAdapter(searchSongAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    rvBaiHatRecent.setLayoutManager(linearLayoutManager);


                    if (baihatrecents.size() > 5)
                        btnViewMore.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    public static boolean CheckinSongRecent(String IdBaihat) {
        if (baihatrecents != null) {
            for (int i = 0; i > baihatrecents.size(); i++)
                if (baihatrecents.get(i).getIdBaiHat().equals(IdBaihat))
                    return true;
        }

        return false;
    }

    private void SetCategoty() {
        DataService dataService = APIService.getService();
        Call<List<ChuDeTheLoai>> callChuDe = dataService.GetAllChuDe();
        Call<List<ChuDeTheLoai>> callTheLoai = dataService.GetAllTheLoai();

        callChuDe.enqueue(new Callback<List<ChuDeTheLoai>>() {
            @Override
            public void onResponse(Call<List<ChuDeTheLoai>> call, Response<List<ChuDeTheLoai>> response) {
                ArrayList<ChuDeTheLoai> ChuDeList = (ArrayList<ChuDeTheLoai>) response.body();
                categoryList = ChuDeList;
            }

            @Override
            public void onFailure(Call<List<ChuDeTheLoai>> call, Throwable t) {

            }
        });

        callTheLoai.enqueue(new Callback<List<ChuDeTheLoai>>() {
            @Override
            public void onResponse(Call<List<ChuDeTheLoai>> call, Response<List<ChuDeTheLoai>> response) {
                ArrayList<ChuDeTheLoai> TheLoaiList = (ArrayList<ChuDeTheLoai>) response.body();
                categoryList.addAll(TheLoaiList);
                categoryAdapter = new AllCategoryAdapter(getContext(), categoryList, 3);
                categoryAdapter.setTongChuDe(categoryList.size() - TheLoaiList.size());
                rvcategory.setAdapter(categoryAdapter);
                rvcategory.setLayoutManager(new GridLayoutManager(getContext(), 2));
            }

            @Override
            public void onFailure(Call<List<ChuDeTheLoai>> call, Throwable t) {

            }
        });

    }

    private void EventClick() {


        btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchSongAdapter != null) {
                    if (searchSongAdapter.isViewMore()) {
                        searchSongAdapter.setViewMore(false);
                        btnViewMore.setText("Xem Thêm");
                    } else {
                        searchSongAdapter.setViewMore(true);
                        btnViewMore.setText("Hiển Thị Ít");
                    }
                }
            }
        });
    }

    public static void addKeyWord(String keyword, TextView textView) {
        if (keyWordArrayList != null) {
            for (int i = 0; i < keyWordArrayList.size(); i++)
                if (keyWordArrayList.get(i).getKeyWord() == keyword)
                    return;
        } else {
            keyWordArrayList = new ArrayList<>();
        }
        Call<String> callback = APIService.getUserService().Search(MainActivity.user.getIdUser(), keyword);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String IdKeyWord = response.body();
                if (!keyword.equals("F")) {
                    {
                        keyWordArrayList.add(0, new KeyWord(IdKeyWord, keyword));
                        if (keyWordArrayList.size() > 15)
                            keyWordArrayList.remove(keyWordArrayList.size() - 1);

                    }

                    Log.e("BBB", IdKeyWord);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }




}