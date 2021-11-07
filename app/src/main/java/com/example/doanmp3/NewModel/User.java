package com.example.doanmp3.NewModel;

public class User {
    private String id;
    private String name;
    private String email;
    private String avatarUri;
    private String bannerUri;
    private String description;

    public User(){

    }

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User(String id, String name, String email, String avatarUri, String bannerUri, String description) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatarUri = avatarUri;
        this.bannerUri = bannerUri;
        this.description = description;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public String getBannerUri() {
        return bannerUri;
    }

    public void setBannerUri(String bannerUri) {
        this.bannerUri = bannerUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", avatarUri='" + avatarUri + '\'' +
                ", bannerUri='" + bannerUri + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
