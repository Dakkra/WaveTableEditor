package com.dakkra.wavetableeditor.ui.harmoniceditor;

import com.dakkra.wavetableeditor.waveconcept.Harmonic;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;

public class HarmonicSlider extends BorderPane {


    private HarmonicSliderListener listener;
    private Harmonic harmonic;
    private Slider slider;

    public HarmonicSlider(Harmonic harmonic, HarmonicSliderListener listener) {
        this.listener = listener;
        this.harmonic = harmonic;
        Slider vSlide = new Slider();
        vSlide.valueProperty().addListener(this::changed);
        String harmNum = "|" + harmonic.getHarmonicValue() + "|";
        vSlide.setOrientation(Orientation.VERTICAL);
        this.setCenter(vSlide);
        this.setBottom(new Label(harmNum));
        this.setPadding(new Insets(1, 3, 1, 3));
        this.slider = vSlide;
    }

    public void reset() {
        slider.setValue(0);
    }

    private void changed(ObservableValue<? extends  Number> observableValue, Number oldValue, Number newValue) {
        listener.update(new Harmonic(this.harmonic.getHarmonicValue(), newValue.floatValue()/100f));
    }

}
