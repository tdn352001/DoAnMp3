package com.example.doanmp3.NewModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Singer implements Parcelable, Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    protected Singer(Parcel in) {
        id = in.readString();
        name = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<Singer> CREATOR = new Creator<Singer>() {
        @Override
        public Singer createFromParcel(Parcel in) {
            return new Singer(in);
        }

        @Override
        public Singer[] newArray(int size) {
            return new Singer[size];
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

    public Object convertToObject(){
        return new Object(this.name, this.thumbnail);
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
    }
}
