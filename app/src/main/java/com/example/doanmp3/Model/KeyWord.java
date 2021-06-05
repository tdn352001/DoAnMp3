package com.example.doanmp3.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class KeyWord {

@SerializedName("IdSearch")
@Expose
private String idSearch;
@SerializedName("KeyWord")
@Expose
private String keyWord;

    public KeyWord(){

    }

    public KeyWord(String idSearch, String keyWord) {
        this.idSearch = idSearch;
        this.keyWord = keyWord;
    }

    public String getIdSearch() {
return idSearch;
}

public void setIdSearch(String idSearch) {
this.idSearch = idSearch;
}

public String getKeyWord() {
return keyWord;
}

public void setKeyWord(String keyWord) {
this.keyWord = keyWord;
}

}