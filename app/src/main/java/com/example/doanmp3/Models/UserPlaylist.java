package com.example.doanmp3.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserPlaylist implements Parcelable {
    private String id;
    private String name;
    private String thumbnail;
    private ArrayList<Song> songs;

    public UserPlaylist(){

    }

    public UserPlaylist(String id, String name, String thumbnail, ArrayList<Song> songs) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.songs = songs;
    }

    protected UserPlaylist(Parcel in) {
        id = in.readString();
        name = in.readString();
        thumbnail = in.readString();
        songs = in.createTypedArrayList(Song.CREATOR);
    }

    public static final Creator<UserPlaylist> CREATOR = new Creator<UserPlaylist>() {
        @Override
        public UserPlaylist createFromParcel(Parcel in) {
            return new UserPlaylist(in);
        }

        @Override
        public UserPlaylist[] newArray(int size) {
            return new UserPlaylist[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public Playlist convertToPlaylist(){
        return new Playlist(this.id, this.name, this.thumbnail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(thumbnail);
        dest.writeTypedList(songs);
    }
}
