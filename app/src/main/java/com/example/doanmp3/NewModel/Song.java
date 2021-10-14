package com.example.doanmp3.NewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Song implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("singers")
    @Expose
    private List<Singer> singers;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("liked")
    @Expose
    private String liked;

    public Song() {
        liked = "0";
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Singer> getSingers() {
        return singers;
    }

    public void setSingers(List<Singer> singers) {
        this.singers = singers;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getAllSingerNames() {
        if (singers == null || singers.size() == 0) {
            return "Unknown";
        }

        StringBuilder singersName = new StringBuilder();
        for(Singer singer : singers){
            singersName.append(singer.getName());
            singersName.append(", ");
        }
        singersName.delete(singersName.length() - 2, singersName.length() - 1);

        return singersName.toString();
    }

    public Object convertToObject(){
        return new Object(this.name, this.thumbnail);
    }
}
