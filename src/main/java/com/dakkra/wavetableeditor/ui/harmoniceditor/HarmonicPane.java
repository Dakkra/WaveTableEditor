package com.dakkra.wavetableeditor.ui.harmoniceditor;

import com.dakkra.wavetableeditor.waveconcept.Harmonic;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;

public class HarmonicPane extends BorderPane {

    private ArrayList<HarmonicSlider> sliders;

    public HarmonicPane(HarmonicSliderListener listener) {
        this.setStyle("-fx-background-color: #000;");
        sliders = new ArrayList<>();
        ScrollPane scrollPane = new ScrollPane();
        FlowPane mainLayout = new FlowPane();
        scrollPane.setContent(mainLayout);
        scrollPane.setFitToWidth(true);
        this.setCenter(scrollPane);
        for (int i = 0; i < 1024; i++) {
            HarmonicSlider slider = new HarmonicSlider(new Harmonic(i + 1, 0), listener);
            sliders.add(slider);
            mainLayout.getChildren().add(slider);
        }
    }

    public void reset() {
        for(HarmonicSlider hs : sliders)
            hs.reset();
    }
}
