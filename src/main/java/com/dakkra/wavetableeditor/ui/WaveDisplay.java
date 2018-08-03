package com.dakkra.wavetableeditor.ui;

import com.dakkra.wavetableeditor.containers.WaveTable;
import com.dakkra.wavetableeditor.containers.WaveTableListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class WaveDisplay extends Pane implements WaveTableListener {

    private Canvas backingCanvas;
    private WaveTable currentWaveTable;
    private GraphicsContext graphics;

    private final Paint BACKGROUND_COLOR = new Color(0.2, 0.2, 0.2, 1);
    private final Paint LINE_COLOR = new Color(0, 0.7, 0.5, 1);

    /**
     * Creates a new WaveDisplay Container
     */
    public WaveDisplay() {
        backingCanvas = new Canvas();
        graphics = backingCanvas.getGraphicsContext2D();
        currentWaveTable = new WaveTable();
        this.getChildren().add(backingCanvas);
        update(currentWaveTable);
    }

    /**
     * Updates this WaveDisplay to render the provided WaveTable
     *
     * @param waveTable WaveTable to render
     */
    public void update(WaveTable waveTable) {
        currentWaveTable = waveTable;
        graphics.setFill(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, backingCanvas.getWidth(), backingCanvas.getHeight());
        short[] samples = currentWaveTable.getSamples();
        graphics.setStroke(LINE_COLOR);
        graphics.setLineWidth(1);
        for (int xIndex = 0; xIndex < samples.length; xIndex++) {
            if (xIndex == 0)
                renderPoint(xIndex, samples[xIndex], xIndex, samples[xIndex]);
            else
                renderPoint(xIndex - 1, samples[xIndex - 1], xIndex, samples[xIndex]);
        }
    }

    /**
     * Renders a point at the desired coordinate and rounds it to the best approximate pixel
     */
    private void renderPoint(double x1, double y1, double x2, double y2) {
        //X values from 0-2043
        //Y values from Short.MIN to Short.MAX
        double relX1 = (x1 / 2048) * backingCanvas.getWidth();
        double relY1 = (Short.MAX_VALUE - y1) / (Short.MAX_VALUE - Short.MIN_VALUE) * backingCanvas.getHeight();
        double relX2 = (x2 / 2048) * backingCanvas.getWidth();
        double relY2 = (Short.MAX_VALUE - y2) / (Short.MAX_VALUE - Short.MIN_VALUE) * backingCanvas.getHeight();
        graphics.strokeLine(relX1, relY1, relX2, relY2);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = snapSize(getWidth()) - x - snappedRightInset();
        final double h = snapSize(getHeight()) - y - snappedBottomInset();
        backingCanvas.setLayoutX(x);
        backingCanvas.setLayoutY(y);
        backingCanvas.setWidth(w);
        backingCanvas.setHeight(h);
        update(currentWaveTable);
    }

}
