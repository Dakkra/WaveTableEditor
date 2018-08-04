package com.dakkra.wavetableeditor.ui.graphicaleditor;

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
        setMinHeight(300);
        setMinWidth(600);
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
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnMouseClicked((e) -> cancelButtonAction());
        Button exportButton = new Button("Accept");
        exportButton.setOnMouseClicked((e) -> System.out.println("ACCEPT HIT"));
        contentFooter.getChildren().add(cancelButton);
        contentFooter.getChildren().add(exportButton);

        show();
    }

    private void cancelButtonAction() {
        hide();
    }
}
