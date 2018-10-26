package com.dakkra.wavetableeditor.ui.harmoniceditor;

import com.dakkra.wavetableeditor.waveconcept.Harmonic;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class HarmonicPane extends BorderPane {

    public HarmonicPane() {
        this.setStyle("-fx-background-color: #000;");

        ScrollPane scrollPane = new ScrollPane();
        FlowPane mainLayout = new FlowPane();
        scrollPane.setContent(mainLayout);
        scrollPane.setFitToWidth(true);
        this.setCenter(scrollPane);
        for (int i = 0; i < 1000; i++)
            mainLayout.getChildren().add(new HarmonicSlider(new Harmonic(i, 0)));
    }
}
