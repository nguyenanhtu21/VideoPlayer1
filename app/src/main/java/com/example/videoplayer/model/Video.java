package com.example.videoplayer.model;

public class Video {
    String id, title, path, thumbnailUri, size, duration;

    public Video(String id,String title, String path, String thumbnailUri, String size, String duration) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.thumbnailUri = thumbnailUri;
        this.size = size;
        this.duration = duration;
    }

    public Video(String title, String path, String thumbnail) {
        this.title = title;
        this.path = path;
        this.thumbnailUri = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnailUri() {
        return thumbnailUri;
    }

    public void setThumbnailUri(String thumbnailUri) {
        this.thumbnailUri = thumbnailUri;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbnail() {
        return thumbnailUri;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnailUri = thumbnail;
    }
}
