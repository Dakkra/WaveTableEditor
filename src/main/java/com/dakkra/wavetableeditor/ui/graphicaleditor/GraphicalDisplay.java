package com.dakkra.wavetableeditor.ui.graphicaleditor;

import com.dakkra.wavetableeditor.waveconcept.WaveTable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;

public class GraphicalDisplay extends Pane {
    private static final Paint BACKGROUND_COLOR = new Color(0.2, 0.2, 0.2, 1);
    private static final Paint LINE_COLOR = new Color(0, 0.7, 0.5, 1);
    private static final Paint EDGE_COLOR = new Color(.7, .7, .3, 1);
    private static final int NODE_RADIUS = 7;

    private Canvas backingCanvas;
    private GraphicsContext graphics;
    private ArrayList<ScalarCircle> circles;
    private EdgeScalarCircle startCircle, endCircle;

    public GraphicalDisplay() {
        reset();
    }

    public void render() {
        graphics.setFill(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, backingCanvas.getWidth(), backingCanvas.getHeight());

        //Horizontal 0-line
        graphics.setStroke(new Color(0.5, 0.5, 0.5, 1));
        graphics.setLineWidth(2);
        graphics.strokeLine(0, backingCanvas.getHeight() / 2, backingCanvas.getWidth(), backingCanvas.getHeight() / 2);

        //Vertical quarter lines
        double canvasW = backingCanvas.getWidth();
        double canvasH = backingCanvas.getHeight();
        graphics.setLineWidth(1);
        graphics.strokeLine(canvasW / 4, 0, canvasW / 4, canvasH);
        graphics.strokeLine(canvasW / 2, 0, canvasW / 2, canvasH);
        graphics.strokeLine(canvasW / 4 + canvasW / 2, 0, canvasW / 4 + canvasW / 2, canvasH);

        graphics.setStroke(LINE_COLOR);
        graphics.setLineWidth(2);

        //Render nodes
        ScalarCircle previousCircle = startCircle;
        if (circles.size() > 0) {
            for (int i = 0; i < circles.size(); i++) {
                graphics.strokeLine(previousCircle.getCenterX(), previousCircle.getCenterY(), circles.get(i).getCenterX(), circles.get(i).getCenterY());
                previousCircle = circles.get(i);
            }
        }
        graphics.strokeLine(previousCircle.getCenterX(), previousCircle.getCenterY(), endCircle.getCenterX(), endCircle.getCenterY());
    }

    public WaveTable encode() {
        ArrayList<ScalarCircle> nodes = new ArrayList<>();
        nodes.add(startCircle);
        nodes.addAll(circles);
        nodes.add(endCircle);
        return ScalarNodesToWaveTable.scalarNodesListToTable(nodes);
    }

    public void reset() {
        this.backingCanvas = new Canvas();
        backingCanvas.setOnMouseClicked((event -> handleCanvasClick(event)));
        this.graphics = backingCanvas.getGraphicsContext2D();
        this.getChildren().add(backingCanvas);
        circles = new ArrayList<>();
        startCircle = new EdgeScalarCircle(0, 0.5f);
        endCircle = new EdgeScalarCircle(1, 0.5f);
        startCircle.setOnMouseDragged(event -> handleCircleDrag(event, startCircle));
        endCircle.setOnMouseDragged(event -> handleCircleDrag(event, endCircle));
        startCircle.setFill(EDGE_COLOR);
        endCircle.setFill(EDGE_COLOR);
        startCircle.setRadius(NODE_RADIUS);
        endCircle.setRadius(NODE_RADIUS);
        this.getChildren().addAll(startCircle, endCircle);
        render();
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
        //Adjust circles
        for (ScalarCircle c : circles) {
            c.setCenterX(c.getScalarX() * backingCanvas.getWidth());
            c.setCenterY(c.getScalarY() * backingCanvas.getHeight());
        }
        startCircle.setCenterX(startCircle.getScalarX() * backingCanvas.getWidth());
        endCircle.setCenterX(endCircle.getScalarX() * backingCanvas.getWidth());
        startCircle.setCenterY(startCircle.getScalarY() * backingCanvas.getHeight());
        endCircle.setCenterY(endCircle.getScalarY() * backingCanvas.getHeight());
        Collections.sort(circles);
        render();
    }

    private void handleCanvasClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            addPoint(mouseEvent.getX(), mouseEvent.getY());
        }
    }

    private void addPoint(double x, double y) {
        ScalarCircle c = new ScalarCircle();
        c.setCenterX(x);
        c.setCenterY(y);
        c.setRadius(NODE_RADIUS);
        c.setFill(LINE_COLOR);
        c.setOnMouseClicked((event -> handleCircleClick(event, c)));
        c.setOnMouseDragged((event -> handleCircleDrag(event, c)));
        circles.add(c);
        Collections.sort(circles);
        //Add scalar digital point to graph
        double scalarX = c.getCenterX() / backingCanvas.getWidth();
        double scalarY = c.getCenterY() / backingCanvas.getHeight();
        c.setScalar(scalarX, scalarY);
        this.getChildren().add(c);
    }

    private void handleCircleClick(MouseEvent event, ScalarCircle circle) {
        if (event.getButton().equals(MouseButton.SECONDARY)) {
            circles.remove(circle);
            getChildren().remove(circle);
        }
    }

    private void handleCircleDrag(MouseEvent event, ScalarCircle circle) {
        if (event.getX() > 0 && event.getX() < backingCanvas.getWidth())
            circle.setCenterX(event.getX());
        if (event.getY() >= 0 && event.getY() <= backingCanvas.getHeight())
            circle.setCenterY(event.getY());
        if (event.getY() < 0)
            circle.setCenterY(1);
        if (event.getY() > backingCanvas.getHeight() - 1)
            circle.setCenterY(backingCanvas.getHeight());
        double scalarX = circle.getCenterX() / backingCanvas.getWidth();
        double scalarY = circle.getCenterY() / backingCanvas.getHeight();
        circle.setScalar(scalarX, scalarY);
        Collections.sort(circles);
        render();
    }

}
