package com.slideshow.patterns.iterator;

import com.slideshow.domain.Slide;

import java.util.List;

public class SlideShowIterator implements SlideIterator {
    private final List<Slide> slides;
    private int pos = 0;

    public SlideShowIterator(List<Slide> slides) {
        this.slides = slides;
    }

    @Override
    public boolean hasNext() {
        return pos < slides.size() - 1;
    }

    @Override
    public boolean hasPrev() {
        return pos > 0;
    }

    @Override
    public Slide next() {
        return slides.get(++pos);
    }

    @Override
    public Slide prev() {
        return slides.get(--pos);
    }

    @Override
    public Slide first() {
        pos = 0;
        return slides.isEmpty() ? null : slides.get(0);
    }

    @Override
    public Slide last() {
        if (slides.isEmpty()) return null;
        pos = slides.size() - 1;
        return slides.get(pos);
    }

    @Override
    public Slide current() {
        return slides.isEmpty() ? null : slides.get(pos);
    }

    @Override
    public int currentIndex() {
        return pos;
    }

    @Override
    public int size() {
        return slides.size();
    }
}
