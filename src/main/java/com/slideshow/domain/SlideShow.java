package com.slideshow.domain;

import com.slideshow.patterns.iterator.SlideIterator;
import com.slideshow.patterns.iterator.SlideShowIterator;

import java.util.*;

public class SlideShow {
    private String name = "Новый показ";
    private final List<Slide> slides = new ArrayList<>();

    public void addSlide(Slide s) {
        slides.add(s);
    }

    public void removeSlide(int idx) {
        if (idx >= 0 && idx < slides.size()) slides.remove(idx);
    }

    public void reorder(int from, int to) {
        if (from < 0 || from >= slides.size() || to < 0 || to > slides.size()) return;
        Slide s = slides.remove(from);
        slides.add(Math.min(to, slides.size()), s);
    }

    public SlideIterator iterator() {
        return new SlideShowIterator(slides);
    }

    public int size() {
        return slides.size();
    }

    public List<Slide> getSlides() {
        return slides;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        this.name = n;
    }
}
