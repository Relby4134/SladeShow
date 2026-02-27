package com.slideshow.patterns.iterator;

import com.slideshow.domain.Slide;

public interface SlideIterator {
    boolean hasNext();

    boolean hasPrev();

    Slide next();

    Slide prev();

    Slide first();

    Slide last();

    Slide current();

    int currentIndex();

    int size();
}
