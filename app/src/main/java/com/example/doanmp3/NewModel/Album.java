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
    private List<Singer> Singers;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("thumbnail")
    @Expose
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Singer> getSingers() {
        return Singers;
    }

    public void setSingers(List<Singer> singers) {
        Singers = singers;
    }
}
