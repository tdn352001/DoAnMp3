package com.example.doanmp3.NewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slide {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("song")
    @Expose
    private Song song;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
