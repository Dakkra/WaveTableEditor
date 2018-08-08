package com.dakkra.wavetableeditor.ui.graphicaleditor;

import com.dakkra.wavetableeditor.ApplicationData;
import com.dakkra.wavetableeditor.waveconcept.WaveTable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GraphicalEditor extends Stage {

    private GraphicalDisplay display;

    public GraphicalEditor() {
        setTitle("Graphical Editor");
        setMinWidth(600);
        setMinHeight(400);
        display = new GraphicalDisplay();
        BorderPane mainLayout = new BorderPane();
        Scene scene = new Scene(mainLayout, 1280, 720);
        setScene(scene);

        //Header
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #336699; -fx-alignment: center; -fx-font-size: 1cm;");
        Text headerTitle = new Text("Graphical Editor");
        headerTitle.setFill(Color.WHITE);
        header.getChildren().add(headerTitle);
        mainLayout.setTop(header);

        mainLayout.setCenter(display);

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
    }

    private void resetButtonAction() {
        display.reset();
    }

    private void acceptButtonAction() {
        WaveTable wt = display.encode();
        ApplicationData.getMasterWaveTable().setSamples(wt.getSamples());
        hide();
    }

    private void cancelButtonAction() {
        hide();
    }
}
