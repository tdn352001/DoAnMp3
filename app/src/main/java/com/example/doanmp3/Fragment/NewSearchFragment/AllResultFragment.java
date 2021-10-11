package com.example.doanmp3.Fragment.NewSearchFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmp3.NewAdapter.AlbumAdapter;
import com.example.doanmp3.NewAdapter.PlaylistAdapter;
import com.example.doanmp3.NewAdapter.SingerAdapter;
import com.example.doanmp3.NewAdapter.SongAdapter;
import com.example.doanmp3.NewModel.Album;
import com.example.doanmp3.NewModel.Playlist;
import com.example.doanmp3.NewModel.ResultSearch;
import com.example.doanmp3.NewModel.Singer;
import com.example.doanmp3.NewModel.Song;
import com.example.doanmp3.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllResultFragment extends Fragment {

    View view;
    RecyclerView rvSong, rvAlbum, rvSinger, rvPlaylist;
    MaterialButton btnViewMoreSong, btnViewMoreAlbum, btnViewMoreSinger, btnViewMorePlaylist;
    LinearLayout layoutContainer, layoutSong, layoutAlbum, layoutSinger, layoutPlaylist;
    TabLayout tabLayout;

    SongAdapter songAdapter;
    AlbumAdapter albumAdapter;
    SingerAdapter singerAdapter;
    PlaylistAdapter playlistAdapter;

    ArrayList<Song> songs;
    ArrayList<Album> albums;
    ArrayList<Singer> singers;
    ArrayList<Playlist> playlists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_result, container, false);
        InitControls();
        InitSongData();
        InitAlbumData();
        InitSingerData();
        InitPlaylistData();
        SetUpSongRecyclerView();
        SetUpAlbumRecyclerView();
        SetUpSingerRecyclerView();
        SetUpPlaylistRecyclerView();
        HandleEvent();
        return view;
    }


    private void InitControls() {
        rvSong = view.findViewById(R.id.rv_song_result_of_all);
        rvAlbum = view.findViewById(R.id.rv_album_result_of_all);
        rvSinger = view.findViewById(R.id.rv_singer_result_of_all);
        rvPlaylist = view.findViewById(R.id.rv_playlist_result_of_all);
        btnViewMoreSong = view.findViewById(R.id.btn_view_more_song_result_of_all);
        btnViewMoreAlbum = view.findViewById(R.id.btn_view_more_album_result_of_all);
        btnViewMoreSinger = view.findViewById(R.id.btn_view_more_singer_result_of_all);
        btnViewMorePlaylist = view.findViewById(R.id.btn_view_more_playlist_result_of_all);
        layoutContainer = view.findViewById(R.id.layout_all_result_container);
        layoutSong = view.findViewById(R.id.layout_song_result_of_all);
        layoutAlbum = view.findViewById(R.id.layout_album_result_of_all);
        layoutSinger = view.findViewById(R.id.layout_singer_result_of_all);
        layoutPlaylist = view.findViewById(R.id.layout_playlist_result_of_all);
    }

    private void InitSongData() {
        songs = new ArrayList<>();
        songAdapter = new SongAdapter(getActivity(), songs, new SongAdapter.ItemClick() {
            @Override
            public void itemClick(int position) {
                Log.e("EEE", "Item Click");
            }

            @Override
            public void optionClick(int position) {
                Log.e("EEE", "option Click");
            }
        });
    }

    private void InitAlbumData() {
    }

    {

        albums = new ArrayList<>();
        albumAdapter = new AlbumAdapter(getContext(), albums, new AlbumAdapter.ItemClick() {
            @Override
            public void itemClick(int position) {
                Log.e("EEE", "Item Click");
            }

            @Override
            public void optionClick(int position) {
                Log.e("EEE", "option Click");
            }
        });
    }

    private void InitSingerData() {
        singers = new ArrayList<>();
        singerAdapter = new SingerAdapter(getActivity(), singers, new SingerAdapter.ItemClick() {
            @Override
            public void itemClick(int position) {
                Log.e("EEE", "Item Click");
            }

            @Override
            public void optionClick(int position) {
                Log.e("EEE", "option Click");
            }
        });
    }

    private void InitPlaylistData() {
        playlists = new ArrayList<>();
        playlistAdapter = new PlaylistAdapter(getActivity(), playlists, new PlaylistAdapter.ItemClick() {
            @Override
            public void itemClick(int position) {
                Log.e("EEE", "Item Click");
            }

            @Override
            public void optionClick(int position) {
                Log.e("EEE", "option Click");
            }
        });
    }

    private void SetUpSongRecyclerView() {
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvSong.setAdapter(songAdapter);
        rvSong.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvSong.setLayoutAnimation(layoutAnimation);
    }

    private void SetUpAlbumRecyclerView() {
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvAlbum.setAdapter(albumAdapter);
        rvAlbum.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvAlbum.setLayoutAnimation(layoutAnimation);
    }

    private void SetUpSingerRecyclerView() {
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvSinger.setAdapter(singerAdapter);
        rvSinger.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvSinger.setLayoutAnimation(layoutAnimation);
    }

    private void SetUpPlaylistRecyclerView() {
        LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvPlaylist.setAdapter(playlistAdapter);
        rvPlaylist.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvPlaylist.setLayoutAnimation(layoutAnimation);

    }

    public void DisplayResult(ResultSearch resultSearch, TabLayout tab) {
        tabLayout = tab;
        if (resultSearch == null) {
            return;
        }
        DisplaySongResult(resultSearch.getSongs());
        DisplayAlbumResult(resultSearch.getAlbums());
        DisplaySingerResult(resultSearch.getSingers());
        DisplayPlaylistResult(resultSearch.getPlaylists());
    }


    @SuppressLint("NotifyDataSetChanged")
    private void DisplaySongResult(List<Song> songsResult) {
        if (songsResult == null) {
            layoutSong.setVisibility(View.GONE);
            return;
        }

        if (songsResult.size() > 5) {
            for (int i = 0; i < 5; i++) {
                songs.add(songsResult.get(i));
            }
        } else {
            songs.addAll(songsResult);
        }
        songAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void DisplayAlbumResult(List<Album> albumsResult) {
        if (albumsResult == null) {
            layoutAlbum.setVisibility(View.GONE);
            return;
        }

        if (albumsResult.size() > 5) {
            for (int i = 0; i < 5; i++) {
                albums.add(albumsResult.get(i));
            }
        } else {
            albums.addAll(albumsResult);
        }
        albumAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void DisplaySingerResult(List<Singer> singersResult) {
        if (singersResult == null) {
            layoutSinger.setVisibility(View.GONE);
            return;
        }

        if (singersResult.size() > 5) {
            for (int i = 0; i < 5; i++) {
                singers.add(singersResult.get(i));
            }
        } else {
            singers.addAll(singersResult);
        }
        singerAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void DisplayPlaylistResult(List<Playlist> playlistsResult) {
        if (playlistsResult == null) {
            layoutPlaylist.setVisibility(View.GONE);
            return;
        }

        if (playlistsResult.size() > 5) {
            for (int i = 0; i < 5; i++) {
                playlists.add(playlistsResult.get(i));
            }
        } else {
            playlists.addAll(playlistsResult);
        }
        playlistAdapter.notifyDataSetChanged();
    }

    private void HandleEvent() {
        btnViewMoreSong.setOnClickListener(v -> ViewMoreClick(1));
        btnViewMoreAlbum.setOnClickListener(v -> ViewMoreClick(2));
        btnViewMoreSinger.setOnClickListener(v -> ViewMoreClick(3));
        btnViewMorePlaylist.setOnClickListener(v -> ViewMoreClick(4));
    }

    private void ViewMoreClick(int id) {
        if (tabLayout == null) {
            return;
        }
        if (id < tabLayout.getTabCount()) {
            Objects.requireNonNull(tabLayout.getTabAt(id)).select();
        }
    }

}