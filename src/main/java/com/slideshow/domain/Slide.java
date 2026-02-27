package com.slideshow.domain;

import javafx.scene.image.Image;

import java.util.*;

public class Slide {
    private final Image image;
    private final String imagePath;
    private final List<GraphicObject> graphics;
    private final String notes;
    private final AnimationSettings animation;
    private final AudioTrack audio;
    private final String title;

    private Slide(Builder b) {
        this.image = b.image;
        this.imagePath = b.imagePath;
        this.graphics = Collections.unmodifiableList(new ArrayList<>(b.graphics));
        this.notes = b.notes;
        this.animation = b.animation;
        this.audio = b.audio;
        this.title = b.title;
    }

    public Image getImage() {
        return image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public List<GraphicObject> getGraphics() {
        return graphics;
    }

    public String getNotes() {
        return notes;
    }

    public AnimationSettings getAnimation() {
        return animation;
    }

    public AudioTrack getAudio() {
        return audio;
    }

    public String getTitle() {
        return title;
    }

    // ── Builder ──────────────────────────────────────────────────
    public static class Builder {
        private final Image image;
        private final String imagePath;

        private List<GraphicObject> graphics = new ArrayList<>();
        private String notes = "";
        private AnimationSettings animation = AnimationSettings.none();
        private AudioTrack audio = null;
        private String title = "Без названия";

        public Builder(Image image, String imagePath) {
            this.image = image;
            this.imagePath = imagePath;
        }

        public Builder notes(String v) {
            this.notes = v;
            return this;
        }

        public Builder animation(AnimationSettings v) {
            this.animation = v;
            return this;
        }

        public Builder audio(AudioTrack v) {
            this.audio = v;
            return this;
        }

        public Builder title(String v) {
            this.title = v;
            return this;
        }

        public Builder addGraphic(GraphicObject v) {
            this.graphics.add(v);
            return this;
        }

        public Slide build() {
            return new Slide(this);
        }
    }
}
