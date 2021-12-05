package com.example.doanmp3.Context.Data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.doanmp3.Interface.DataService;
import com.example.doanmp3.Models.Album;
import com.example.doanmp3.Models.Song;
import com.example.doanmp3.Service.APIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserData {

    static FirebaseUser user;
    static ArrayList<Song> loveSongs;
    static DatabaseReference loveSongRef;
    static ArrayList<Album> albums;


    static boolean loadingData;

    public static void InitUserInfo() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        loveSongRef = FirebaseDatabase.getInstance().getReference("likes").child("songs");
        loadingData = false;
    }


    // Love Song
    public static ArrayList<Song> getLoveSongs() {
        return loveSongs;
    }

    public static void GetLoveSongData() {
        if (user == null)
            return;
        loadingData = true;
        String userId = user.getUid();
        DataService dataService = APIService.getService();
        Call<List<Song>> callback = dataService.getUserLoveSongs(userId);
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                loveSongs = (ArrayList<Song>) response.body();
                if (loveSongs == null) {
                    loveSongs = new ArrayList<>();
                }
                loadingData = false;
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                if (loveSongs == null) {
                    loveSongs = new ArrayList<>();
                }
                loadingData = false;
            }
        });
    }

    public static boolean isLoveSong(Song song) {
        int position = FindLoveSong(song);
        return position != -1;
    }

    public static void addLoveSong(Song song, boolean changeDatabase) {
        if (song == null || loveSongs == null)
            return;
        boolean isLove = isLoveSong(song);
        if (!isLove) {
            loveSongs.add(0, song);
            if (changeDatabase) {
                SaveLoveSongInDatabase(song.getId());
                SaveLoveSongInFirebase(song.getId());
            }
        }
    }

    public static int FindLoveSong(Song song) {
        if (song == null || loveSongs == null)
            return -1;
        for (int i = 0; i < loveSongs.size(); i++) {
            if (loveSongs.get(i).getId().equals(song.getId())) {
                return i;
            }
        }
        return -1;
    }

    public static void removeLoveSong(String songId, boolean changeDatabase) {
        if (songId.equals(""))
            return;

        for (int i = 0; i < loveSongs.size(); i++) {
            if (loveSongs.get(i).getId().equals(songId)) {
                loveSongs.remove(i);
                if (changeDatabase) {
                    SaveLoveSongInDatabase(songId);
                    SaveLoveSongInFirebase(songId);
                }
                return;
            }
        }
    }

    public static void AddOrRemoveSong(Song song, boolean changeDatabase) {
        int position = FindLoveSong(song);
        if (position == -1) {
            loveSongs.add(0, song);
        } else {
            loveSongs.remove(position);
        }
        if (changeDatabase) {
            SaveLoveSongInDatabase(song.getId());
            SaveLoveSongInFirebase(song.getId());
        }
    }

    private static void SaveLoveSongInDatabase(String songId) {
        if (user == null || songId.equals(""))
            return;

        DataService dataService = APIService.getService();
        Call<Void> callback = dataService.loveSong(user.getUid(), songId);
        callback.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

            }
        });
    }

    private static void SaveLoveSongInFirebase(String songId) {
        if (songId.equals("") || user == null)
            return;
        ArrayList<String> listUserLoveSong = new ArrayList<>();
        loveSongRef.child(songId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    listUserLoveSong.add(dataSnapshot.getValue(String.class));
                }
                if (listUserLoveSong.contains(user.getUid())) {
                    listUserLoveSong.remove(user.getUid());
                } else
                    listUserLoveSong.add(user.getUid());


                Log.e("EEE", "User love song change: " + songId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        loveSongRef.child(songId).setValue(listUserLoveSong);
    }

    public static boolean isLoadingData() {
        return loadingData;
    }

    // Favorite Albums

    public static ArrayList<Album> getFavoriteAlbums() {
        return albums;
    }

    public static void GetFavoriteAlbumData() {
        if (user == null)
            return;
        loadingData = true;
        String userId = user.getUid();
        DataService dataService = APIService.getService();
        Call<List<Album>> callback = dataService.getUserFavoriteAlbums(userId);
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(@NonNull Call<List<Album>> call, @NonNull Response<List<Album>> response) {
                albums = (ArrayList<Album>) response.body();
                if(albums == null)
                    albums = new ArrayList<>();
                loadingData =false;
            }

            @Override
            public void onFailure(@NonNull Call<List<Album>> call, @NonNull Throwable t) {
                loadingData =false;
            }
        });
    }

    public static int FindFavoriteAlbum(Album album){
        if (album == null || albums == null)
            return -1;
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getId().equals(album.getId())) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isFavoriteAlbum(Album album) {
        int position = FindFavoriteAlbum(album);
        return position != -1;
    }

    public static void addFavoriteAlbum(Album album, boolean changeDatabase){
        if (album == null || loveSongs == null)
            return;
        boolean isLove = isFavoriteAlbum(album);
        if (!isLove) {
            albums.add(0, album);
            if (changeDatabase) {
//                SaveLoveSongInDatabase(album.getId());
//                SaveLoveSongInFirebase(album.getId());
            }
        }
    }

    public static void removeFavoriteAlbum(String albumId, boolean changeDatabase) {
        if (albumId.equals(""))
            return;

        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getId().equals(albumId)) {
                albums.remove(i);
                if (changeDatabase) {
//                    SaveLoveSongInDatabase(albumId);
//                    SaveLoveSongInFirebase(albumId);
                }
                return;
            }
        }
    }

    public static void AddOrRemoveFavoriteAlbum(Album album, boolean changeDatabase) {
        int position = FindFavoriteAlbum(album);
        if (position == -1) {
            albums.add(0, album);
        } else {
            albums.remove(position);
        }
        if (changeDatabase) {
            SaveLoveSongInDatabase(album.getId());
            SaveLoveSongInFirebase(album.getId());
        }
    }

    public static void ClearData() {
        user = null;
        loveSongs.clear();
        loveSongs = null;
        loveSongRef = null;
    }

}
