package com.example.videoplayer.model;

import java.util.ArrayList;

public class Playlist {
    String  name;
    String id;
    ArrayList<Video> videoList;

    public Playlist(String name, String id, ArrayList<Video> videoList) {
        this.name = name;
        this.id = id;
        this.videoList = videoList;
    }

    public ArrayList<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(ArrayList<Video> videoList) {
        this.videoList = videoList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
