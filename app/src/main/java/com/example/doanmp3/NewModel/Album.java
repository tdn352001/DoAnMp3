package com.example.doanmp3.NewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Album implements Serializable {
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
        singers = singers;
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
