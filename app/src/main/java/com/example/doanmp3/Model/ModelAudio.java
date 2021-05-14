package com.example.doanmp3.Model;

import android.net.Uri;

import com.example.doanmp3.R;

import java.util.Collections;

public class ModelAudio {

    String audioTitle;
    String audioDuration;
    String audioArtist;
    Uri audioUri;

    public ModelAudio() {
    }

    public ModelAudio(String audioTitle, String audioDuration, String audioArtist, Uri audioUri) {
        this.audioTitle = audioTitle;
        this.audioDuration = audioDuration;
        this.audioArtist = audioArtist;
        this.audioUri = audioUri;
    }

    public String getaudioTitle() {
        return audioTitle;
    }

    public void setaudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public String getaudioDuration() {
        return audioDuration;
    }

    public void setaudioDuration(String audioDuration) {
        this.audioDuration = audioDuration;
    }

    public String getaudioArtist() {
        return audioArtist;
    }

    public void setaudioArtist(String audioArtist) {
        this.audioArtist = audioArtist;
    }

    public Uri getaudioUri() {
        return audioUri;
    }

    public void setaudioUri(Uri audioUri) {
        this.audioUri = audioUri;
    }

    public BaiHat convertBaiHat(){
        BaiHat baiHat = new BaiHat();
        baiHat.setCaSi(Collections.singletonList(audioArtist));
        baiHat.setIdBaiHat("-1");
        baiHat.setTenBaiHat(audioTitle);
        baiHat.setLinkBaiHat(audioUri.toString());
        baiHat.setHinhBaiHat("R.drawable.img_disknhac");

        return  baiHat;
    }

}