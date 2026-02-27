package com.slideshow.infrastructure;

import javafx.scene.media.*;

import java.io.File;

public class AudioPlayer {
    private MediaPlayer player;

    public void play(String path) {
        stop();
        try {
            player = new MediaPlayer(new Media(new File(path).toURI().toString()));
            player.play();
        } catch (Exception e) {
            System.err.println("AudioPlayer: " + e.getMessage());
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
            player = null;
        }
    }

    public void pause() {
        if (player != null) player.pause();
    }

    public void resume() {
        if (player != null) player.play();
    }
}
