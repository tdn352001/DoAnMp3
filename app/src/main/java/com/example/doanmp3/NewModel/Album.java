package com.example.doanmp3.NewModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Album implements Parcelable {
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

    protected Album(Parcel in) {
        id = in.readString();
        name = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Singer> getSingers() {
        return singers;
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
        return new Object(this.id, this.name, this.thumbnail);
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
