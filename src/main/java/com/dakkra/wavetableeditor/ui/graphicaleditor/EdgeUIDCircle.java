package com.dakkra.wavetableeditor.ui.graphicaleditor;

public class EdgeUIDCircle extends UIDCircle {

    private final double fixedScalarX;
    private double scalarY;

    public EdgeUIDCircle(double fixedScalarX, double scalarY) {
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
