package com.dakkra.wavetableeditor.ui.graphicaleditor;

public class EdgeScalarCircle extends ScalarCircle {

    private final double fixedScalarX;
    private double scalarY;

    public EdgeScalarCircle(double fixedScalarX, double scalarY) {
        this.fixedScalarX = fixedScalarX;
        this.scalarY = scalarY;
    }

    @Override
    public void setScalar(double scalarX, double scalarY) {
        this.scalarY = scalarY;
    }

    @Override
    public double getScalarX() {
        return fixedScalarX;
    }

    @Override
    public double getScalarY() {
        return scalarY;
    }

}
