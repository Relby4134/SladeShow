package com.slideshow.infrastructure;

import com.google.gson.*;
import com.slideshow.domain.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void save(SlideShow show, String dirPath) throws IOException {
        new File(dirPath).mkdirs();
        JsonObject root = new JsonObject();
        root.addProperty("name", show.getName());
        JsonArray arr = new JsonArray();
        for (Slide s : show.getSlides()) {
            JsonObject o = new JsonObject();
            o.addProperty("imagePath", s.getImagePath());
            o.addProperty("title", s.getTitle());
            o.addProperty("notes", s.getNotes());
            JsonObject anim = new JsonObject();
            anim.addProperty("type", s.getAnimation().getType().name());
            anim.addProperty("durationMs", s.getAnimation().getDurationMs());
            o.add("animation", anim);
            if (s.getAudio() != null) {
                JsonObject au = new JsonObject();
                au.addProperty("path", s.getAudio().getPath());
                au.addProperty("name", s.getAudio().getName());
                o.add("audio", au);
            }
            JsonArray gArr = new JsonArray();
            for (GraphicObject g : s.getGraphics()) {
                JsonObject go = new JsonObject();
                go.addProperty("type", g.getType().name());
                go.addProperty("content", g.getContent());
                go.addProperty("x", g.getX());
                go.addProperty("y", g.getY());
                go.addProperty("width", g.getWidth());
                go.addProperty("height", g.getHeight());
                go.addProperty("color", g.getColor());
                gArr.add(go);
            }
            o.add("graphics", gArr);
            arr.add(o);
        }
        root.add("slides", arr);
        File cfg = new File(dirPath, "config.json");
        try (Writer w = new OutputStreamWriter(new FileOutputStream(cfg), StandardCharsets.UTF_8)) {
            GSON.toJson(root, w);
        }
    }

    public SlideShow load(String dirPath) throws IOException {
        File cfg = new File(dirPath, "config.json");
        if (!cfg.exists()) throw new FileNotFoundException("config.json not found in " + dirPath);
        JsonObject root;
        try (Reader r = new InputStreamReader(new FileInputStream(cfg), StandardCharsets.UTF_8)) {
            root = JsonParser.parseReader(r).getAsJsonObject();
        }
        SlideShow show = new SlideShow();
        show.setName(root.get("name").getAsString());
        FileManager fm = new FileManager();
        for (JsonElement el : root.getAsJsonArray("slides")) {
            JsonObject o = el.getAsJsonObject();
            String imgPath = o.get("imagePath").getAsString();
            try {
                javafx.scene.image.Image img = fm.loadImage(imgPath);
                Slide.Builder b = new Slide.Builder(img, imgPath)
                        .title(o.get("title").getAsString())
                        .notes(o.get("notes").getAsString());
                if (o.has("animation")) {
                    JsonObject an = o.getAsJsonObject("animation");
                    AnimationSettings.Type t = AnimationSettings.Type.valueOf(an.get("type").getAsString());
                    long ms = an.get("durationMs").getAsLong();
                    b.animation(switch (t) {
                        case FADE_IN -> AnimationSettings.fadeIn(ms);
                        case SLIDE_IN -> AnimationSettings.slideIn(ms);
                        case ZOOM_IN -> AnimationSettings.zoomIn(ms);
                        default -> AnimationSettings.none();
                    });
                }
                if (o.has("audio")) {
                    JsonObject au = o.getAsJsonObject("audio");
                    b.audio(new AudioTrack(au.get("path").getAsString(), au.get("name").getAsString()));
                }
                if (o.has("graphics")) {
                    for (JsonElement ge : o.getAsJsonArray("graphics")) {
                        JsonObject go = ge.getAsJsonObject();
                        b.addGraphic(new GraphicObject(
                                GraphicObject.Type.valueOf(go.get("type").getAsString()),
                                go.get("content").getAsString(),
                                go.get("x").getAsDouble(), go.get("y").getAsDouble(),
                                go.get("width").getAsDouble(), go.get("height").getAsDouble(),
                                go.get("color").getAsString()));
                    }
                }
                show.addSlide(b.build());
            } catch (Exception e) {
                System.err.println("Skip slide " + imgPath + ": " + e.getMessage());
            }
        }
        return show;
    }
}
