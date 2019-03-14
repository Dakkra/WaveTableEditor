package com.dakkra.wavetableeditor.ui.harmoniceditor;

import com.dakkra.wavetableeditor.ApplicationData;
import com.dakkra.wavetableeditor.ui.PrimaryUI;
import com.dakkra.wavetableeditor.ui.WaveDisplay;
import com.dakkra.wavetableeditor.waveconcept.Harmonic;
import com.dakkra.wavetableeditor.waveconcept.WaveTable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.HashMap;

public class HarmonicEditor extends Stage {

    private WaveTable localWavetable;
    private HarmonicPane harmonicPane;
    private HashMap<Integer, Harmonic> harmonicChanges;

    public HarmonicEditor() {
        localWavetable = new WaveTable();
        WaveDisplay localDisplay = new WaveDisplay();
        harmonicPane = new HarmonicPane(this::sliderListener);
        harmonicChanges = new HashMap<>();
        localWavetable.addWaveTableListener(localDisplay);
        setTitle("Harmonic Editor");
        setMinWidth(PrimaryUI.MINIMUM_WIDTH);
        setMinHeight(PrimaryUI.MINIMUM_HEIGHT);
        BorderPane mainLayout = new BorderPane();

        //Header
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #336699; -fx-alignment: center; -fx-font-size: 1cm;");
        Text headerTitle = new Text("Harmonic Editor");
        headerTitle.setFill(Color.WHITE);
        header.getChildren().add(headerTitle);
        mainLayout.setTop(header);

        //Center
        GridPane grid = new GridPane();
        ColumnConstraints constraints = new ColumnConstraints();
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100);
        constraints.setPercentWidth(50);
        grid.setGridLinesVisible(true);
        grid.getColumnConstraints().addAll(constraints, constraints);
        grid.getRowConstraints().addAll(rowConstraints);
        grid.add(harmonicPane, 0, 0);
        grid.add(localDisplay, 1, 0);
        mainLayout.setCenter(grid);

        //Export Footer
        HBox contentFooter = new HBox();
        mainLayout.setBottom(contentFooter);
        contentFooter.setStyle("-fx-background-color: #224466; -fx-alignment: center-right; -fx-font-size: .5cm;");
        Button resetButton = new Button("Reset");
        resetButton.setOnMouseClicked(event -> resetButtonAction());
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnMouseClicked((e) -> cancelButtonAction());
        Button exportButton = new Button("Accept");
        exportButton.setOnMouseClicked((e) -> acceptButtonAction());
        contentFooter.getChildren().addAll(resetButton, cancelButton, exportButton);

        Scene scene = new Scene(mainLayout, PrimaryUI.DEFAULT_WIDTH, PrimaryUI.DEFAULT_HEIGHT);
        setScene(scene);
    }

    /**
     * Updates/generates the waveform based upon the harmonic sliders
     */
    private void updateWaveform() {
        Collection<Harmonic> harmonics = harmonicChanges.values();
        Harmonic[] harmArr = new Harmonic[harmonics.size()];
        //This is slow as hell, need to fix
        int counter = 0;
        for (Harmonic h : harmonics) {
            harmArr[counter++] = h;
        }
        localWavetable.generateFromHarmonics(harmArr);
    }

    /**
     * Slider listener for harmonics
     */
    private void sliderListener(Harmonic harmonic) {
        if (harmonic.getAmplitude() > 0)
            harmonicChanges.put(harmonic.getHarmonicValue(), harmonic);
        else
            harmonicChanges.remove(harmonic.getHarmonicValue());
        updateWaveform();
    }

    private void acceptButtonAction() {
        ApplicationData.getMasterWaveTable().setSamples(localWavetable.getSamples());
        hide();
    }

    private void cancelButtonAction() {
        hide();
    }

    private void resetButtonAction() {
        harmonicPane.reset();
    }
}
