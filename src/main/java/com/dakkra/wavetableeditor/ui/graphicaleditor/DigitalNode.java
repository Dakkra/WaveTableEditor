package com.dakkra.wavetableeditor.ui.graphicaleditor;

public class DigitalNode implements Comparable<DigitalNode> {
    private double x, y;

    public DigitalNode(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public int compareTo(DigitalNode other) {
        if (this.x < other.x)
            return -1;
        if (this.x > other.x)
            return 1;
        return 0;
    }
}
