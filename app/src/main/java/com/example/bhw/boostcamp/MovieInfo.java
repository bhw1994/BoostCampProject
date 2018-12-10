package com.example.bhw.boostcamp;

import android.util.Log;

import org.json.JSONObject;

public class MovieInfo {
    public MovieInfo(JSONObject data){
        try
        {
            title=data.getString("title");
            link=data.getString("link");
            imageUrl=data.getString("image");
            year=data.getInt("pubDate");
            director=data.getString("director");
            userRating=(float) data.getDouble("userRating");
            title=data.getString("title");
            actors=data.getString("actor");


        }
        catch (Exception e)
        {
            Log.d("result","MovieInfo constructor error");
        }
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public float getUserRating() {
        return userRating/2;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    String title;
    String link;
    String imageUrl;
    String director;
    String actors;

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    float userRating;
    int year;
//    TextView intro;

}
