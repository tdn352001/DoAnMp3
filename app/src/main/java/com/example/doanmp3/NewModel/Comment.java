package com.example.doanmp3.NewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Comment {
    private String id;
    private String idUser;
    private String nameUser;
    private String thumbnailUser;
    private String content;
    private Date createdAt;
    private List<String> liked;


    public Comment() {
    }

    public Comment(String id, String idUser, String nameUser, String thumbnailUser, String content) {
        this.id = id;
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.thumbnailUser = thumbnailUser;
        this.content = content;
        this.createdAt = new Date();
        this.liked = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getThumbnailUser() {
        return thumbnailUser;
    }

    public void setThumbnailUser(String thumbnailUser) {
        this.thumbnailUser = thumbnailUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getLiked() {
        return liked;
    }

    public void setLiked(List<String> liked) {
        this.liked = liked;
    }
}
