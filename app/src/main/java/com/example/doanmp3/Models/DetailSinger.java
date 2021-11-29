package com.example.doanmp3.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DetailSinger {
    @SerializedName("songs")
    @Expose
    private List<Song> songs;

    @SerializedName("albums")
    @Expose
    private List<Album> albums;

    public DetailSinger(List<Song> songs, List<Album> albums) {
        this.songs = songs;
        this.albums = albums;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public List<Object> getObjectAlbums(){
        List<Object> objectAlbums = new ArrayList<>();
        if(albums == null) return  objectAlbums;

        for (Album aLbum : albums){
            objectAlbums.add(aLbum.convertToObject());
        }

        return  objectAlbums;
    }

}
