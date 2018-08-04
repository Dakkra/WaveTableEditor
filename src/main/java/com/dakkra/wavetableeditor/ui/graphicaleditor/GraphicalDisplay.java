package com.dakkra.wavetableeditor.ui.graphicaleditor;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GraphicalDisplay extends Pane {

    private Canvas backingCanvas;
    private GraphicsContext graphics;

    private final Paint BACKGROUND_COLOR = new Color(0.2, 0.2, 0.2, 1);
    private final Paint LINE_COLOR = new Color(0, 0.7, 0.5, 1);

    public GraphicalDisplay() {
        this.backingCanvas = new Canvas();
        this.graphics = backingCanvas.getGraphicsContext2D();
        this.getChildren().add(backingCanvas);
        render();
    }

    public void render() {
        graphics.setFill(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, backingCanvas.getWidth(), backingCanvas.getHeight());
    }

    @Override
    public void layoutChildren() {
        super.layoutChildren();
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = snapSize(getWidth()) - x - snappedRightInset();
        final double h = snapSize(getHeight()) - y - snappedBottomInset();
        backingCanvas.setLayoutX(x);
        backingCanvas.setLayoutY(y);
        backingCanvas.setWidth(w);
        backingCanvas.setHeight(h);
        render();
    }

}
