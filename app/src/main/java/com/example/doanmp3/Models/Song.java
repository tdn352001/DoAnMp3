package com.example.doanmp3.Models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Song implements Parcelable {
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

    private boolean isAudio;

    public Song() {
        liked = "0";
        isAudio = false;
    }


    protected Song(Parcel in) {
        id = in.readString();
        singers = in.createTypedArrayList(Singer.CREATOR);
        name = in.readString();
        thumbnail = in.readString();
        link = in.readString();
        liked = in.readString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isAudio= in.readBoolean();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeTypedList(singers);
        dest.writeString(name);
        dest.writeString(thumbnail);
        dest.writeString(link);
        dest.writeString(liked);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(isAudio);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
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

    public boolean isAudio() {
        return isAudio;
    }

    public void setAudio(boolean audio) {
        isAudio = audio;
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
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", singers=" + singers +
                ", name='" + name + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", link='" + link + '\'' +
                ", liked='" + liked + '\'' +
                '}';
    }
}
