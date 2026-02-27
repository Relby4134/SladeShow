package com.slideshow.domain;

public class GraphicObject {
    public enum Type {TEXT, RECTANGLE, CIRCLE, ARROW}

    private final Type type;
    private final String content;
    private final double x, y, width, height;
    private final String color;

    public GraphicObject(Type type, String content,
                         double x, double y, double width, double height, String color) {
        this.type = type;
        this.content = content;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public Type getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public String getColor() {
        return color;
    }
}
