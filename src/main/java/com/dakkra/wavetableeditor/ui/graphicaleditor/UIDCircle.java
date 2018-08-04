package com.dakkra.wavetableeditor.ui.graphicaleditor;

import javafx.scene.shape.Circle;

import java.util.UUID;

public class UIDCircle extends Circle implements Comparable<UIDCircle> {
    private UUID uid = UUID.randomUUID();
    private double scalarX, scalarY;

    public UUID getUid() {
        return uid;
    }

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
    public int compareTo(UIDCircle other) {
        if (this.getCenterX() < other.getCenterX())
            return -1;
        if (this.getCenterX() > other.getCenterX())
            return 1;
        return 0;
    }
}
