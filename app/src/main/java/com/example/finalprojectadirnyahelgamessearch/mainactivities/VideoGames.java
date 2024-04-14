package com.example.finalprojectadirnyahelgamessearch.mainactivities;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoGames {
    private String title;
    private String thumbnail;
    private String short_description;
    private String description;
    private String genre;
    private String developer;
    public String platform;
    private String releaseDate; // Dictionary-style storage


    public VideoGames(String title, String thumbnail, String genre, String platform, String releaseDate, String short_description, String developer) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.genre=genre;
        this.platform= platform;
        this.releaseDate = releaseDate;
        this.short_description = short_description;
        this.developer = developer;

    }

    public String getDescription() {
        return description;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDeveloper() {
        return developer;
    }
    public void setDeveloper(String developer) {
        this.developer = developer;
    }
    public VideoGames(){}
    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public String getShort_description() {
        return short_description;
    }
    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }
    public String getGenre() {
        return genre;
    }
    public String getTitle() {
        return title;
    }


    public String getPlatform() {
        return platform;
    }
}
