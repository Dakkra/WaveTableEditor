package com.dakkra.wavetableeditor.ui.graphicaleditor;

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
    private final Paint BACKGROUND_COLOR = new Color(0.2, 0.2, 0.2, 1);
    private final Paint LINE_COLOR = new Color(0, 0.7, 0.5, 1);
    private final Paint EDGE_COLOR = new Color(.7, .7, .3, 1);

    private Canvas backingCanvas;
    private GraphicsContext graphics;
    private ArrayList<UIDCircle> circles;
    private EdgeUIDCircle startCircle, endCircle;

    public GraphicalDisplay() {
        this.backingCanvas = new Canvas();
        backingCanvas.setOnMouseClicked((event -> handleCanvasClick(event)));
        this.graphics = backingCanvas.getGraphicsContext2D();
        this.getChildren().add(backingCanvas);
        circles = new ArrayList<>();
        startCircle = new EdgeUIDCircle(0, 0.5f);
        endCircle = new EdgeUIDCircle(1, 0.5f);
        startCircle.setOnMouseDragged(event -> handleCircleDrag(event, startCircle));
        endCircle.setOnMouseDragged(event -> handleCircleDrag(event, endCircle));
        startCircle.setFill(EDGE_COLOR);
        endCircle.setFill(EDGE_COLOR);
        startCircle.setRadius(10);
        endCircle.setRadius(10);
        this.getChildren().addAll(startCircle, endCircle);
        render();
    }

    public void render() {
        graphics.setFill(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, backingCanvas.getWidth(), backingCanvas.getHeight());

        //Horizontal 0-line
        graphics.setStroke(new Color(0.5, 0.5, 0.5, 1));
        graphics.setLineWidth(2);
        graphics.strokeLine(0, backingCanvas.getHeight() / 2, backingCanvas.getWidth(), backingCanvas.getHeight() / 2);

        graphics.setStroke(LINE_COLOR);
        graphics.setLineWidth(2);

        //Render nodes
        UIDCircle previousCircle = startCircle;
        if (circles.size() > 0) {
            for (int i = 0; i < circles.size(); i++) {
                graphics.strokeLine(previousCircle.getCenterX(), previousCircle.getCenterY(), circles.get(i).getCenterX(), circles.get(i).getCenterY());
                previousCircle = circles.get(i);
            }
        }
        graphics.strokeLine(previousCircle.getCenterX(), previousCircle.getCenterY(), endCircle.getCenterX(), endCircle.getCenterY());
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
        for (UIDCircle c : circles) {
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
        UIDCircle c = new UIDCircle();
        c.setCenterX(x);
        c.setCenterY(y);
        c.setRadius(10);
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

    private void handleCircleClick(MouseEvent event, UIDCircle circle) {
        if (event.getButton().equals(MouseButton.SECONDARY)) {
            circles.remove(circle);
            getChildren().remove(circle);
        }
    }

    private void handleCircleDrag(MouseEvent event, UIDCircle circle) {
        if (event.getX() > 0 && event.getX() < backingCanvas.getWidth())
            circle.setCenterX(event.getX());
        if (event.getY() >= 0 && event.getY() <= backingCanvas.getHeight())
            circle.setCenterY(event.getY());
        double scalarX = circle.getCenterX() / backingCanvas.getWidth();
        double scalarY = circle.getCenterY() / backingCanvas.getHeight();
        circle.setScalar(scalarX, scalarY);
        Collections.sort(circles);
        render();
    }

}
