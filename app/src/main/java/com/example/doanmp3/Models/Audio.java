package com.example.doanmp3.Models;

import com.example.doanmp3.Context.Data.AudioThumbnail;

import java.util.Collections;

public class Audio {
    String title;
    String artist;
    String uri;

    public Audio(){}

    public Audio(String audioTitle, String audioArtist, String audioUri) {
        this.title = audioTitle;
        this.artist = audioArtist;
        this.uri = audioUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }



    public Song convertToSong(){
        Song song = new Song();
        song.setId("-1");
        song.setName(this.title);
        song.setSingers(Collections.singletonList(new Singer("-1", this.artist, "null")));
        song.setAudio(true);
        song.setThumbnail(AudioThumbnail.getRandomThumbnail().toString());
        song.setLink(this.uri);
        song.setLiked("0");
        return song;
    }
}
