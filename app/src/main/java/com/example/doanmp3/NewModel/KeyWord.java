package com.example.doanmp3.NewModel;


public class KeyWord {


    private String id;
    private String keyWord;

    public KeyWord() {
    }

    public KeyWord(String id, String keyWord) {
        this.id = id;
        this.keyWord = keyWord;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

}