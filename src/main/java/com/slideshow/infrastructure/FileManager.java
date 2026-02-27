package com.slideshow.infrastructure;

import javafx.scene.image.Image;

import java.io.File;
import java.util.*;

public class FileManager {
    public static final String DEFAULT_PATH = "slides/";

    public Image loadImage(String path) {
        File f = new File(path);
        if (!f.exists()) throw new IllegalArgumentException("File not found: " + path);
        return new Image(f.toURI().toString());
    }

    public boolean isImageFile(String name) {
        String low = name.toLowerCase();
        return low.endsWith(".png") || low.endsWith(".jpg")
                || low.endsWith(".jpeg") || low.endsWith(".gif") || low.endsWith(".bmp");
    }

    public List<File> listImages(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.isDirectory()) return Collections.emptyList();
        File[] files = dir.listFiles(f -> f.isFile() && isImageFile(f.getName()));
        if (files == null) return Collections.emptyList();
        List<File> list = new ArrayList<>(Arrays.asList(files));
        list.sort(Comparator.comparing(File::getName));
        return list;
    }
}
