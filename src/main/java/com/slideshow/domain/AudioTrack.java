package com.slideshow.domain;

public class AudioTrack {
    private final String path;
    private final String name;

    public AudioTrack(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
