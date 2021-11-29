package com.example.doanmp3.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResultSearch implements Serializable {
    @SerializedName("songs")
    @Expose
    private List<Song> songs;

    @SerializedName("albums")
    @Expose
    private List<Album> albums;

    @SerializedName("singers")
    @Expose
    private List<Singer> singers;

    @SerializedName("playlists")
    @Expose
    private List<Playlist> playlists;

    public List<Song> getSongs() {
        return songs;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public List<Singer> getSingers() {
        return singers;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public boolean isEmpty(){
        int i = 0;
        if(songs == null || songs.size() == 0) i++;
        if(albums == null || albums.size() == 0) i++;
        if(singers == null || singers.size() == 0) i++;
        if(playlists == null || playlists.size() == 0) i++;

        return i == 4;
    }
}
