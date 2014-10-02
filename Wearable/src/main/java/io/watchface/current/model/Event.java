package io.watchface.current.model;

public class Event {
    private float begin;
    private float end;
    private int color;

    public Event(float begin, float end, int color) {
        this.begin = begin;
        this.end = end;
        this.color = color;
    }

    public float getBegin() {
        return begin;
    }

    public float getEnd() {
        return end;
    }

    public int getColor() {
        return color;
    }
}
