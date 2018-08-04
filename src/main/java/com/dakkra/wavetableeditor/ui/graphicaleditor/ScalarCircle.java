package com.dakkra.wavetableeditor.ui.graphicaleditor;

import javafx.scene.shape.Circle;

public class ScalarCircle extends Circle implements Comparable<ScalarCircle> {
    private double scalarX, scalarY;

    public void setScalar(double scalarX, double scalarY) {
        this.scalarX = scalarX;
        this.scalarY = scalarY;
    }

    public double getScalarX() {
        return scalarX;
    }

    public double getScalarY() {
        return scalarY;
    }

    @Override
    public int compareTo(ScalarCircle other) {
        if (this.getScalarX() < other.getScalarX())
            return -1;
        if (this.getScalarX() > other.getScalarX())
            return 1;
        return 0;
    }
}
