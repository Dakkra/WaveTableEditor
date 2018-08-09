package com.dakkra.wavetableeditor.ui.harmoniceditor;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class HarmonicPane extends Pane {

    public HarmonicPane() {
        ScrollPane scrollPane = new ScrollPane();
        HBox mainLayout = new HBox();
        scrollPane.setContent(mainLayout);
        scrollPane.setFitToWidth(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        getChildren().addAll(scrollPane);

        for (int i = 0; i < 50; i++)
            mainLayout.getChildren().add(new Button("Button" + i));
    }
}
