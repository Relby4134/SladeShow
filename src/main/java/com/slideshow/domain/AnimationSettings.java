package com.slideshow.domain;

public class AnimationSettings {
    public enum Type {NONE, FADE_IN, SLIDE_IN, ZOOM_IN}

    private final Type type;
    private final long durationMs;

    private AnimationSettings(Type type, long durationMs) {
        this.type = type;
        this.durationMs = durationMs;
    }

    public static AnimationSettings none() {
        return new AnimationSettings(Type.NONE, 0);
    }

    public static AnimationSettings fadeIn(long ms) {
        return new AnimationSettings(Type.FADE_IN, ms);
    }

    public static AnimationSettings slideIn(long ms) {
        return new AnimationSettings(Type.SLIDE_IN, ms);
    }

    public static AnimationSettings zoomIn(long ms) {
        return new AnimationSettings(Type.ZOOM_IN, ms);
    }

    public Type getType() {
        return type;
    }

    public long getDurationMs() {
        return durationMs;
    }

    @Override
    public String toString() {
        return type + "(" + durationMs + "ms)";
    }
}
