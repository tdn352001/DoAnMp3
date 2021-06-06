package com.example.doanmp3.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.example.doanmp3.Model.Album;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.CaSi;
import com.example.doanmp3.Model.ChuDeTheLoai;
import com.example.doanmp3.Model.KeyWord;
import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.R;
import com.example.doanmp3.Service.APIService;
import com.example.doanmp3.Service.DataService;
import com.google.android.material.button.MaterialButton;
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
    public ArrayList<KeyWord> keyWordArrayList;

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


    /* Phần Result*/
    CoordinatorLayout ResultLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapterSearch;

    int progress = 0; // Tiến Trình Lấy Kết Quả
    ProgressDialog progressDialog;

    // Mảng Kết Quả
    public static ArrayList<BaiHat> baiHats;
    public static ArrayList<Album> albums;
    public static ArrayList<CaSi> caSis;
    public static ArrayList<Playlist> playlists;


    // Fragment kết quả
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
                    progressDialog = ProgressDialog.show(getContext(), "Đang Lấy Dữ Liêu", "Loading....!", false, false);
                    SearchBaiHat(query);
                    SearchAlbum(query);
                    SearchCaSi(query);
                    SearchPlaylist(query);
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
        GetRecentSong();
        SetCategoty();
        RecentEventClick();
    }

    // Lấy Từ Khóa Tìm Kiếm Gần Đây

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
            }
        });
    }

    // SetLayout Flow

    public void SetFlowLayout() {
        Log.e("BBB", "Set Flowlayout");

        if (flowLayout == null)
            return;
        flowLayout.removeAllViews();
        while (keyWordArrayList.size() > 15)
            keyWordArrayList.remove(keyWordArrayList.size() - 1);

        if (keyWordArrayList != null) {
            if (keyWordArrayList.size() > 0) {
                for (int i = 0; i < keyWordArrayList.size(); i++) {
                    AddViewIntoFlowLayout(keyWordArrayList.get(i));
                }
                SearchRecentLayout.setVisibility(View.VISIBLE);
            } else
                SearchRecentLayout.setVisibility(View.GONE);
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
            Log.e("BBB", keyword);
            Call<String> callback = APIService.getUserService().Search(MainActivity.user.getIdUser(), keyword);
            callback.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String IdKeyWord = (String) response.body();
                    Log.e("BBB", IdKeyWord);
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

    // lấy Bài Hát Nghe Gần Đây
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
                    searchSongAdapter.notifyDataSetChanged();
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

    // Kiểm Tra Bài Hát Có Được Chơi Gần Đây Không
    public static boolean CheckinSongRecent(String IdBaihat) {
        if (baihatrecents != null) {
            for (int i = 0; i > baihatrecents.size(); i++)
                if (baihatrecents.get(i).getIdBaiHat().equals(IdBaihat))
                    return true;
        }

        return false;
    }

    //Lấy Danh Sách Category
    private void SetCategoty() {
        DataService dataService = APIService.getService();
        Call<List<ChuDeTheLoai>> callChuDe = dataService.GetAllChuDe();
        Call<List<ChuDeTheLoai>> callTheLoai = dataService.GetAllTheLoai();

        callChuDe.enqueue(new Callback<List<ChuDeTheLoai>>() {
            @Override
            public void onResponse(Call<List<ChuDeTheLoai>> call, Response<List<ChuDeTheLoai>> response) {
                ArrayList<ChuDeTheLoai> ChuDeList = (ArrayList<ChuDeTheLoai>) response.body();
                categoryList = ChuDeList;
                callTheLoai.enqueue(new Callback<List<ChuDeTheLoai>>() {
                    @Override
                    public void onResponse(Call<List<ChuDeTheLoai>> call, Response<List<ChuDeTheLoai>> response) {
                        ArrayList<ChuDeTheLoai> TheLoaiList = (ArrayList<ChuDeTheLoai>) response.body();
                        if (categoryList != null) {
                            categoryList.addAll(TheLoaiList);
                            categoryAdapter = new AllCategoryAdapter(getContext(), categoryList, 3);
                            categoryAdapter.setTongChuDe(categoryList.size() - TheLoaiList.size());
                            rvcategory.setAdapter(categoryAdapter);
                            rvcategory.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        }

                    }

                    @Override
                    public void onFailure(Call<List<ChuDeTheLoai>> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onFailure(Call<List<ChuDeTheLoai>> call, Throwable t) {

            }
        });


    }

    // Sự kiến click của layout gần đây
    private void RecentEventClick() {

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


    /* ===================== SETUP RESULT ======================*/


    private void SetupResult() {
        setupViewPager();
    }

    private void setupViewPager() {
        adapterSearch = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // List Fragment kết quả
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new AllSearchFragment());
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
        tabLayout.setupWithViewPager(viewPager);

    }

    public void SearchBaiHat(String query) {
        Call<List<BaiHat>> callback = APIService.getService().GetSearchBaiHat(query);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                baiHats = (ArrayList<BaiHat>) response.body();
                searchbaihatFragment.SetRV();
                progress++;
                if (progress > 2)
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    public void SearchAlbum(String query) {
        Call<List<Album>> callback = APIService.getService().GetSearchAlbum(query);
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                albums = (ArrayList<Album>) response.body();
                searchalbumFragment.SetRv();
                progress++;
                if (progress > 2)
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }

    public void SearchCaSi(String query) {
        Call<List<CaSi>> callback = APIService.getService().GetSearchCaSi(query);
        callback.enqueue(new Callback<List<CaSi>>() {
            @Override
            public void onResponse(Call<List<CaSi>> call, Response<List<CaSi>> response) {
                caSis = (ArrayList<CaSi>) response.body();
                searchcasiFragment.SetRV();
                progress++;
                if (progress > 2)
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<CaSi>> call, Throwable t) {

            }
        });
    }

    public void SearchPlaylist(String query) {
        Call<List<Playlist>> callback = APIService.getService().GetSearchPlaylist(query);
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                playlists = (ArrayList<Playlist>) response.body();
                searchplaylistFragment.SetRv();
                progress++;
                if (progress > 2)
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }

}