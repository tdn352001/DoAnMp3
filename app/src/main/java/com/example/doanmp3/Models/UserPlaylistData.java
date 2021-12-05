package com.example.doanmp3.Models;

import java.util.List;

public class UserPlaylistData {
    private String idPlaylist;
    private List<String> idSongs;


    public UserPlaylistData() {
    }

    public UserPlaylistData(String idPlaylist, List<String> idSongs) {
        this.idPlaylist = idPlaylist;
        this.idSongs = idSongs;
    }

    public String getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(String idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public List<String> getIdSongs() {
        return idSongs;
    }

    public void setIdSongs(List<String> idSongs) {
        this.idSongs = idSongs;
    }

    @Override
    public String toString() {
        return "UserPlaylistData{" +
                "idPlaylist='" + idPlaylist + '\'' +
                ", idSongs=" + idSongs +
                '}';
    }
}
