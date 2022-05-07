package com.educorreia.netflixclone.models;

public class Movie {
    private int id;
    private String title;
    private String coverUrl;
    private String description;
    private String cast;

    public Movie() { }

    public Movie(int id, String title, String coverUrl, String description, String cast) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.description = description;
        this.cast = cast;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }
}
