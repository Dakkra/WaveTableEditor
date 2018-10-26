package com.dakkra.wavetableeditor.ui.harmoniceditor;

import com.dakkra.wavetableeditor.waveconcept.Harmonic;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;

public class HarmonicSlider extends BorderPane {

    public HarmonicSlider(Harmonic harmonic) {
        Slider vSlide = new Slider();
        String harmNum = "|" + harmonic.getHarmonicValue() + "|";
        vSlide.setOrientation(Orientation.VERTICAL);
        this.setCenter(vSlide);
        this.setBottom(new Label(harmNum));
        this.setPadding(new Insets(1, 3, 1, 3));
    }

}
