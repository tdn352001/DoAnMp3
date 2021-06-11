package com.example.doanmp3.Fragment.SearchFragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.Adapter.AlbumAdapter;
import com.example.doanmp3.Adapter.AllSingerAdapter;
import com.example.doanmp3.Adapter.PlaylistAdapter;
import com.example.doanmp3.Adapter.SearchSongAdapter;
import com.example.doanmp3.Model.Album;
import com.example.doanmp3.Model.BaiHat;
import com.example.doanmp3.Model.CaSi;
import com.example.doanmp3.Model.Playlist;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class AllSearchFragment extends Fragment {

    View view;
    public int countResult;
    int progress = 0;
    // Layout Kết Quả
    LinearLayout linearLayout;
    RelativeLayout layoutBaihat, layoutCasi, layoutAlbum, layoutPlaylist;

    // Recyclerview
    RecyclerView rvBaiHat, rvAlbum, rvCaSi, rvPlaylist;

    // btn Xem Thêm, noinfo;
    MaterialButton btnBaiHat, btnAlbum, btnCaSi, btnPlaylist, textView;

    // Adapter
    SearchSongAdapter adapterSong;
    AlbumAdapter adapterAlbum;
    AllSingerAdapter adapterSinger;
    PlaylistAdapter adapterPlaylist;

    //Arraylist
    static ArrayList<BaiHat> baiHats;
    static ArrayList<Album> albums;
    static ArrayList<CaSi> caSis;
    static ArrayList<Playlist> playlists;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_search, container, false);
        AnhXa();
        ClickViewMore();
        SaveResultSearch();
        return view;
    }


    private void AnhXa() {
        linearLayout = view.findViewById(R.id.layout_all_result);
        textView = view.findViewById(R.id.txt_search_result_noinfo);
        layoutBaihat = view.findViewById(R.id.layout_result_baihat);
        layoutAlbum = view.findViewById(R.id.layout_result_album);
        layoutCasi = view.findViewById(R.id.layout_result_casi);
        layoutPlaylist = view.findViewById(R.id.layout_result_playlist);
        rvBaiHat = view.findViewById(R.id.rv_result_baihat);
        rvAlbum = view.findViewById(R.id.rv_result_album);
        rvCaSi = view.findViewById(R.id.rv_result_casi);
        rvPlaylist = view.findViewById(R.id.rv_result_playlist);
        btnBaiHat = view.findViewById(R.id.btn_viewmore_baihat_result);
        btnAlbum = view.findViewById(R.id.btn_viewmore_album_result);
        btnCaSi = view.findViewById(R.id.btn_viewmore_casi_result);
        btnPlaylist = view.findViewById(R.id.btn_viewmore_playlist_result);

    }

    private void ClickViewMore() {
        btnBaiHat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SearchFragment.viewPager != null) {
                    SearchFragment.viewPager.setCurrentItem(1);
                }
            }
        });

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SearchFragment.viewPager != null) {
                    SearchFragment.viewPager.setCurrentItem(2);
                }
            }
        });

        btnCaSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SearchFragment.viewPager != null) {
                    SearchFragment.viewPager.setCurrentItem(3);
                }
            }
        });

        btnPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SearchFragment.viewPager != null) {
                    SearchFragment.viewPager.setCurrentItem(4);
                }
            }
        });
    }

    private void SaveResultSearch() {
        countResult = 0;
        progress = 0;
        SetResultBaiHat();
        SetResultAlbum();
        SetResultCaSi();
        SetResultPlaylist();
    }

    private void SetResultBaiHat() {
        if (baiHats != null) {
            if (baiHats.size() > 0) {
                rvBaiHat.removeAllViews();
                adapterSong = new SearchSongAdapter(getContext(), baiHats, true, true);
                rvBaiHat.setAdapter(adapterSong);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                rvBaiHat.setLayoutManager(linearLayoutManager);
                progress++;
                if (progress > 0) {
                    textView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                return;
            }
            layoutBaihat.setVisibility(View.GONE);
            if (progress == 4 && countResult == 0) {
                linearLayout.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText("Không Tìm Thấy Kết Quả");
            }
        }




    }

    private void SetResultAlbum() {
        if (albums != null) {
            if (albums.size() > 0) {
                rvAlbum.removeAllViews();
                adapterAlbum = new AlbumAdapter(getContext(), albums, true);
                rvAlbum.setAdapter(adapterAlbum);
                rvAlbum.setLayoutManager(new GridLayoutManager(getContext(), 2));
                progress++;
                if (progress > 0) {
                    textView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                return;
            }
            layoutAlbum.setVisibility(View.GONE);
            if (progress == 4 && countResult == 0) {
                linearLayout.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText("Không Tìm Thấy Kết Quả");
            }
        }


    }

    private void SetResultCaSi() {
        if (caSis != null) {
            if (caSis.size() > 0) {
                rvCaSi.removeAllViews();
                adapterSinger = new AllSingerAdapter(getContext(), caSis, true);
                rvCaSi.setAdapter(adapterSinger);
                rvCaSi.setLayoutManager(new GridLayoutManager(getContext(), 2));
                progress++;
                if (progress > 0) {
                    textView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }

                return;
            }
            layoutCasi.setVisibility(View.GONE);
            if (progress == 4 && countResult == 0) {
                linearLayout.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText("Không Tìm Thấy Kết Quả");
            }
        }


    }

    private void SetResultPlaylist() {
        if (playlists != null) {
            if (playlists.size() > 0) {
                rvPlaylist.removeAllViews();
                adapterPlaylist = new PlaylistAdapter(getContext(), SearchFragment.playlists, true);
                rvPlaylist.setLayoutManager(new GridLayoutManager(getContext(), 2));
                rvPlaylist.setAdapter(adapterPlaylist);
                progress++;
                if (progress > 0) {
                    textView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                return;

            }
            layoutPlaylist.setVisibility(View.GONE);
            if (progress == 4 && countResult == 0) {
                linearLayout.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText("Không Tìm Thấy Kết Quả");
            }
        }


    }

    public void GetResult() {
        Handler handler = new Handler();


        Runnable runbaihat = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);

                // Tìm Kiếm Bài Hát
                if (SearchFragment.baiHats != null) {
                    progress++;
                    baiHats = SearchFragment.baiHats;
                    if (SearchFragment.baiHats.size() > 0) {
                        adapterSong = new SearchSongAdapter(getContext(), SearchFragment.baiHats, true, true);
                        rvBaiHat.setAdapter(adapterSong);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        rvBaiHat.setLayoutManager(linearLayoutManager);
                        countResult++;
                        if (progress > 0) {
                            textView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        layoutBaihat.setVisibility(View.GONE);
                        if (progress == 4) {
                            if (countResult == 0) {
                                progress = 0;
                                linearLayout.setVisibility(View.GONE);
                                textView.setVisibility(View.VISIBLE);
                                textView.setText("Không Tìm Thấy Kết Quả");
                            } else {
                                textView.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                    handler.removeCallbacks(this);
                }
            }
        };

        // Tìm Tiếm ALbum
        Runnable runalbum = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                if (SearchFragment.albums != null) {
                    albums = SearchFragment.albums;
                    progress++;
                    if (SearchFragment.albums.size() > 0) {
                        adapterAlbum = new AlbumAdapter(getContext(), SearchFragment.albums, true);
                        rvAlbum.setAdapter(adapterAlbum);
                        rvAlbum.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        countResult++;
                        if (progress > 0) {
                            textView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        layoutAlbum.setVisibility(View.GONE);
                        if (progress == 4 && countResult == 0) {
                            linearLayout.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            textView.setText("Không Tìm Thấy Kết Quả");
                        }
                    }

                    handler.removeCallbacks(this);
                }
            }
        };

        // Tìm Kiếm Ca Sĩ
        Runnable runcasi = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                if (SearchFragment.caSis != null) {
                    progress++;
                    caSis = SearchFragment.caSis;
                    if (SearchFragment.caSis.size() > 0) {
                        adapterSinger = new AllSingerAdapter(getContext(), SearchFragment.caSis, true);
                        rvCaSi.setAdapter(adapterSinger);
                        rvCaSi.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        countResult++;
                        if (progress > 0) {
                            textView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        layoutCasi.setVisibility(View.GONE);
                        if (progress == 4 && countResult == 0) {
                            linearLayout.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                    handler.removeCallbacks(this);
                }

            }
        };

        // Tìm Kiếm Playlist

        Runnable runplaylist = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                if (SearchFragment.playlists != null) {
                    progress++;
                    playlists = SearchFragment.playlists;
                    if (SearchFragment.playlists.size() > 0) {
                        adapterPlaylist = new PlaylistAdapter(getContext(), SearchFragment.playlists, true);
                        rvPlaylist.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        rvPlaylist.setAdapter(adapterPlaylist);
                        countResult++;
                        if (progress > 0) {
                            textView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        layoutPlaylist.setVisibility(View.GONE);
                        if (progress == 4 && countResult == 0) {
                            linearLayout.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            textView.setText("Không Tìm Thấy Kết Quả");
                        }

                    }
                    handler.removeCallbacks(this);
                }
            }
        };

        // Kiểm Tra Ánh Xạ
        Runnable run = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                if (rvPlaylist != null) {
                    rvBaiHat.removeAllViews();
                    rvAlbum.removeAllViews();
                    rvCaSi.removeAllViews();
                    rvPlaylist.removeAllViews();
                    textView.setText("Đang Lấy Dữ Liệu");
                    linearLayout.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    progress = 0;
                    countResult = 0;
                    handler.postDelayed(runbaihat, 100);
                    handler.postDelayed(runalbum, 100);
                    handler.postDelayed(runcasi, 100);
                    handler.postDelayed(runplaylist, 100);
                    handler.removeCallbacks(this);
                }
            }
        };


        handler.postDelayed(run, 100);


    }

    public void GetResultBaiHat(){
        Handler handler = new Handler();


        Runnable runbaihat = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);

                // Tìm Kiếm Bài Hát
                if (SearchFragment.baiHats != null) {
                    progress++;
                    baiHats = SearchFragment.baiHats;
                    if (SearchFragment.baiHats.size() > 0) {
                        adapterSong = new SearchSongAdapter(getContext(), SearchFragment.baiHats, true, true);
                        rvBaiHat.setAdapter(adapterSong);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        rvBaiHat.setLayoutManager(linearLayoutManager);
                        countResult++;
                        if (progress > 0) {
                            textView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        layoutBaihat.setVisibility(View.GONE);
                        if (progress == 4) {
                            if (countResult == 0) {
                                progress = 0;
                                linearLayout.setVisibility(View.GONE);
                                textView.setVisibility(View.VISIBLE);
                                textView.setText("Không Tìm Thấy Kết Quả");
                            } else {
                                textView.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                    handler.removeCallbacks(this);
                }
            }
        };

        Runnable run = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                if (rvPlaylist != null) {
                    rvBaiHat.removeAllViews();
                    rvAlbum.removeAllViews();
                    rvCaSi.removeAllViews();
                    rvPlaylist.removeAllViews();
                    textView.setText("Đang Lấy Dữ Liệu");
                    linearLayout.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    progress = 0;
                    countResult = 0;
                    handler.postDelayed(runbaihat, 100);
                    handler.removeCallbacks(this);
                }
            }
        };


        handler.postDelayed(run, 100);
    }

    public void GetResultAlbum(){
        Handler handler = new Handler();
        Runnable runalbum = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                if (SearchFragment.albums != null) {
                    albums = SearchFragment.albums;
                    progress++;
                    if (SearchFragment.albums.size() > 0) {
                        adapterAlbum = new AlbumAdapter(getContext(), SearchFragment.albums, true);
                        rvAlbum.setAdapter(adapterAlbum);
                        rvAlbum.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        countResult++;
                        if (progress > 0) {
                            textView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        layoutAlbum.setVisibility(View.GONE);
                        if (progress == 4 && countResult == 0) {
                            linearLayout.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            textView.setText("Không Tìm Thấy Kết Quả");
                        }
                    }

                    handler.removeCallbacks(this);
                }
            }
        };
        Runnable run = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                if (rvPlaylist != null) {
                    rvBaiHat.removeAllViews();
                    rvAlbum.removeAllViews();
                    rvCaSi.removeAllViews();
                    rvPlaylist.removeAllViews();
                    textView.setText("Đang Lấy Dữ Liệu");
                    linearLayout.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    progress = 0;
                    countResult = 0;
                    handler.postDelayed(runalbum, 100);
                    handler.removeCallbacks(this);
                }
            }
        };


        handler.postDelayed(run, 100);

    }

    public void GetResultCasi(){
        Handler handler = new Handler();

        Runnable runcasi = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                if (SearchFragment.caSis != null) {
                    progress++;
                    caSis = SearchFragment.caSis;
                    if (SearchFragment.caSis.size() > 0) {
                        adapterSinger = new AllSingerAdapter(getContext(), SearchFragment.caSis, true);
                        rvCaSi.setAdapter(adapterSinger);
                        rvCaSi.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        countResult++;
                        if (progress > 0) {
                            textView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        layoutCasi.setVisibility(View.GONE);
                        if (progress == 4 && countResult == 0) {
                            linearLayout.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                    handler.removeCallbacks(this);
                }

            }
        };

        Runnable run = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                if (rvPlaylist != null) {
                    rvBaiHat.removeAllViews();
                    rvAlbum.removeAllViews();
                    rvCaSi.removeAllViews();
                    rvPlaylist.removeAllViews();
                    textView.setText("Đang Lấy Dữ Liệu");
                    linearLayout.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    progress = 0;
                    countResult = 0;
                    handler.postDelayed(runcasi, 100);
                    handler.removeCallbacks(this);
                }
            }
        };


        handler.postDelayed(run, 100);
    }

    public void GetResultPlaylist(){
        Handler handler = new Handler();

        Runnable runplaylist = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                if (SearchFragment.playlists != null) {
                    progress++;
                    playlists = SearchFragment.playlists;
                    if (SearchFragment.playlists.size() > 0) {
                        adapterPlaylist = new PlaylistAdapter(getContext(), SearchFragment.playlists, true);
                        rvPlaylist.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        rvPlaylist.setAdapter(adapterPlaylist);
                        countResult++;
                        if (progress > 0) {
                            textView.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        layoutPlaylist.setVisibility(View.GONE);
                        if (progress == 4 && countResult == 0) {
                            linearLayout.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            textView.setText("Không Tìm Thấy Kết Quả");
                        }

                    }
                    handler.removeCallbacks(this);
                }
            }
        };

        // Kiểm Tra Ánh Xạ
        Runnable run = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                if (rvPlaylist != null) {
                    rvBaiHat.removeAllViews();
                    rvAlbum.removeAllViews();
                    rvCaSi.removeAllViews();
                    rvPlaylist.removeAllViews();
                    textView.setText("Đang Lấy Dữ Liệu");
                    linearLayout.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    progress = 0;
                    countResult = 0;
                    handler.postDelayed(runplaylist, 100);
                    handler.removeCallbacks(this);
                }
            }
        };


        handler.postDelayed(run, 100);

    }
}