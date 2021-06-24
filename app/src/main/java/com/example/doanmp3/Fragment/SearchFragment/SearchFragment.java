package com.example.doanmp3.Fragment.SearchFragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.doanmp3.Activity.MainActivity;
import com.example.doanmp3.Adapter.AllCategoryAdapter;
import com.example.doanmp3.Adapter.SearchSongAdapter;
import com.example.doanmp3.Adapter.ViewPagerAdapter;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.ChuDeTheLoai;
import com.example.doanmp3.Model.KeyWord;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {

    View view;
    SearchView searchView;

    /* Phần RECENT */
    NestedScrollView RecentLayout;
    //     Phần Tìm Kiếm Gần Đây
    RelativeLayout SearchRecentLayout;
    TextView btnDelete;
    FlowLayout flowLayout;
    public static ArrayList<KeyWord> keyWordArrayList;

    // Bài Hát Gần Đây
    public static MaterialButton btnViewMore;
    @SuppressLint("StaticFieldLeak")
    public static RelativeLayout SongRecentLayout;
    RecyclerView rvBaiHatRecent;
    public static ArrayList<BaiHat> baihatrecents;
    @SuppressLint("StaticFieldLeak")
    public static SearchSongAdapter searchSongAdapter;

    //ChuDeTheLoai
    RecyclerView rvcategory;
    public static ArrayList<ChuDeTheLoai> categoryList;
    AllCategoryAdapter categoryAdapter;


    /* Phần Result*/
    CoordinatorLayout ResultLayout;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    ViewPagerAdapter adapterSearch;

    static int progress = 0; // Tiến Trình Lấy Kết Quả
    static ProgressDialog progressDialog;

    // Fragment kết quả
    AllSearchFragment allSearchFragment = new AllSearchFragment();
    SearchbaihatFragment searchbaihatFragment = new SearchbaihatFragment();
    SearchalbumFragment searchalbumFragment = new SearchalbumFragment();
    SearchcasiFragment searchcasiFragment = new SearchcasiFragment();
    SearchplaylistFragment searchplaylistFragment = new SearchplaylistFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_search, container, false);
        AnhXa();
        setupSearchView();
        SetupReCent();
        SetupResult();
        return view;
    }


    private void AnhXa() {
        searchView = view.findViewById(R.id.search_view);

        /* Phần Recent*/

        RecentLayout = view.findViewById(R.id.scrollview_recent);

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

        /* Phần Result */
        ResultLayout = view.findViewById(R.id.scrollview_result);
        tabLayout = view.findViewById(R.id.tablayout_result);
        viewPager = view.findViewById(R.id.viewpager_search);


    }


    // Setup Search View
    private void setupSearchView() {

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.equals("")) {
                    // Lưu Từ Khóa
                    addKeyWord(query);

                    //Hiển THị màn Hình kết quả, ẩn màn hình gần đây
                    ResultLayout.setVisibility(View.VISIBLE);
                    RecentLayout.setVisibility(View.GONE);
                    viewPager.setCurrentItem(0);

                    // Lấy Dữ Liệu.
                    progress = 0;
                    progressDialog = ProgressDialog.show(getContext(), "Đang Lấy Dữ Liêu", "Loading....!", false, true);
                    allSearchFragment.Search(query);
                    searchbaihatFragment.Search(query);
                    searchalbumFragment.Search(query);
                    searchcasiFragment.Search(query);
                    searchplaylistFragment.Search(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    ResultLayout.setVisibility(View.GONE);
                    RecentLayout.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

    }

    /* ===================== SETUP RECENT======================*/

    private void SetupReCent() {
        GetResentKeyWord();
        GetBaiHatRecent();
        SetCategoty();
        RecentEventClick();
    }

    // Lấy Từ Khóa Tìm Kiếm Gần Đây

    private void GetResentKeyWord() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 250);
                SearchRecentLayout.setVisibility(View.GONE);
                if (keyWordArrayList != null) {
                    SetFlowLayout();
                    handler.removeCallbacks(this);
                }
            }
        };

        handler.postDelayed(runnable, 250);
    }

    // SetLayout Flow
    public void SetFlowLayout() {


        if (flowLayout == null)
            return;
        flowLayout.removeAllViews();

        while (keyWordArrayList.size() > 15)
            keyWordArrayList.remove(keyWordArrayList.size() - 1);


        if (keyWordArrayList.size() > 0) {
            for (int i = 0; i < keyWordArrayList.size(); i++) {
                AddViewIntoFlowLayout(keyWordArrayList.get(i));
            }
            SearchRecentLayout.setVisibility(View.VISIBLE);
        } else
            SearchRecentLayout.setVisibility(View.GONE);

    }

    //Thêm View vào layout
    public void AddViewIntoFlowLayout(KeyWord keyWord) {

        TextView textView = new TextView(getContext());
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(35, 35, 0, 0);
        textView.setLayoutParams(params);
        textView.setId(Integer.parseInt(keyWord.getIdSearch()));
        textView.setText(keyWord.getKeyWord());
        textView.setPadding(15, 5, 15, 5);
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.custom_item_search_recent);
        textView.setTextColor(getResources().getColor(R.color.purple_500));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery(keyWord.getKeyWord(), true);
            }
        });
        flowLayout.addView(textView);
    }

    // Thêm từ khóa tìm Kiếm
    public void addKeyWord(String keyword) {
        if (keyWordArrayList == null)
            keyWordArrayList = new ArrayList<>();
        if (!checkSearchBefore(keyword)) {
            Call<String> callback = APIService.getUserService().Search(MainActivity.user.getIdUser(), keyword);
            callback.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String IdKeyWord = (String) response.body();
                    if (!IdKeyWord.equals("F")) {
                        KeyWord keyWord = new KeyWord(IdKeyWord, keyword);
                        keyWordArrayList.add(0, keyWord);
                        SetFlowLayout();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }

    }

    boolean checkSearchBefore(String query) {
        if (keyWordArrayList != null) {
            for (int i = 0; i < keyWordArrayList.size(); i++)
                if (keyWordArrayList.get(i).getKeyWord().equals(query))
                    return true;
        }

        return false;
    }

    private void SetRVRecent() {
        if (baihatrecents != null) {
            if (baihatrecents.size() > 0)
                SongRecentLayout.setVisibility(View.VISIBLE);

            searchSongAdapter = new SearchSongAdapter(getContext(), baihatrecents, false, true);
            rvBaiHatRecent.setAdapter(searchSongAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            rvBaiHatRecent.setLayoutManager(linearLayoutManager);

            if (baihatrecents.size() > 5)
                btnViewMore.setVisibility(View.VISIBLE);

        }
    }

    // lấy Bài Hát Nghe Gần Đây
    public void GetBaiHatRecent() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatRecent(MainActivity.user.getIdUser());
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                SearchFragment.baihatrecents = (ArrayList<BaiHat>) response.body();
                if (SearchFragment.baihatrecents == null) {
                    SearchFragment.baihatrecents = new ArrayList<>();
                }
                SetRVRecent();
            }
            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    // Thêm Bài Hát Gần Đây
    public static void AddBaiHatRecent(BaiHat baiHat) {
        int i = 0;
        while (i < baihatrecents.size()) {
            if (baihatrecents.get(i).getIdBaiHat().equals(baiHat.getIdBaiHat())) {
                baihatrecents.remove(i);
                break;
            }
            i++;
        }
        baihatrecents.add(0, baiHat);
        searchSongAdapter.notifyDataSetChanged();
        if (baihatrecents.size() > 4)
            btnViewMore.setVisibility(View.VISIBLE);

        if (SongRecentLayout.getVisibility() == View.GONE)
            SongRecentLayout.setVisibility(View.VISIBLE);

    }

    //Lấy Danh Sách Category
    private void SetCategoty() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 300);
                if (categoryList != null) {
                    categoryAdapter = new AllCategoryAdapter(getContext(), categoryList, 3);
                    categoryAdapter.setTongChuDe(categoryList.size() - MainActivity.theloailist.size());
                    rvcategory.setAdapter(categoryAdapter);
                    rvcategory.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    handler.removeCallbacks(this);
                }
            }
        };

        handler.postDelayed(runnable, 300);
    }

    // Sự kiến click của layout gần đây
    private void RecentEventClick() {

        btnDelete.setOnClickListener(v -> {

            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext());
            dialog.setTitle("Xóa Lịch Sử Tìm Kiếm!");
            dialog.setMessage("Bạn có Chắc Chắn?");
            dialog.setIcon(R.drawable.error);
            dialog.setBackground(getResources().getDrawable(R.drawable.custom_diaglog_background));
            dialog.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });

            dialog.setNegativeButton("Thực Hiện", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Call<String> callback = APIService.getUserService().DeleteSearch(MainActivity.user.getIdUser());
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String rel = (String) response.body();
                            if (rel != null)
                                if (rel.equals("S")) {
                                    keyWordArrayList.clear();
                                    SetFlowLayout();
                                }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            });

            dialog.show();
        });

        btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchSongAdapter != null) {
                    if (searchSongAdapter.isViewMore()) {
                        searchSongAdapter.setViewMore(false);
                        btnViewMore.setText("Hiển Thị Ít");
                    } else {
                        searchSongAdapter.setViewMore(true);
                        btnViewMore.setText("Xem Thêm");
                    }
                }
            }
        });
    }


    /* ===================== SETUP RESULT ======================*/

    private void SetupResult() {
        adapterSearch = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // List Fragment kết quả
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(allSearchFragment);
        fragments.add(searchbaihatFragment);
        fragments.add(searchalbumFragment);
        fragments.add(searchcasiFragment);
        fragments.add(searchplaylistFragment);

        // List Title Tablayout
        ArrayList<String> titles = new ArrayList<>();
        titles.add("Tất Cả");
        titles.add("Bài Hát");
        titles.add("Album");
        titles.add("Ca Sĩ");
        titles.add("Playlist");

        // Set list fragment + title cho adapter
        adapterSearch.setList(fragments);
        adapterSearch.setTitle(titles);

        //Hoàn Thành Set up
        viewPager.setAdapter(adapterSearch);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);

    }

    public static void CountResult(){
        progress++;
        if(progress > 3){
            progressDialog.dismiss();
        }
    }




}