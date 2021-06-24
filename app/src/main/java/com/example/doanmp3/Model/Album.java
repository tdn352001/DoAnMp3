package com.example.doanmp3.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Album implements Serializable {
    @SerializedName("IdAlbum")
    @Expose
    private String idAlbum;
    @SerializedName("TenCaSi")
    @Expose
    private String tenCaSi;
    @SerializedName("TenAlbum")
    @Expose
    private String tenAlbum;
    @SerializedName("HinhAlbum")
    @Expose
    private String hinhAlbum;

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getTenCaSi() {
        return tenCaSi;
    }

    public void setTenCaSi(String tenCaSi) {
        this.tenCaSi = tenCaSi;
    }

    public String getTenAlbum() {
        return tenAlbum;
    }


    public String getHinhAlbum() {
        return hinhAlbum;
    }

}
