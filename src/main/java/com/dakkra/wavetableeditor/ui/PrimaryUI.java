package com.dakkra.wavetableeditor.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PrimaryUI extends Application {

    /**
     * Launches this JavaFX GUI
     */
    public static void startUserInterface() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //TODO Build the GUI
        primaryStage.setTitle("Wave Table Editor");
        BorderPane mainLayout = new BorderPane();

        //Header
        HBox header = new HBox();
        mainLayout.setTop(header);
        header.setStyle("-fx-background-color: #336699; -fx-alignment: center; -fx-font-size: 1cm;");
        Text headerLabel = new Text("Wave Table Editor");
        headerLabel.setFill(Color.WHITE);
        header.getChildren().add(headerLabel);

        //Footer
        HBox footer = new HBox();
        mainLayout.setBottom(footer);
        footer.setStyle("-fx-background-color: #336699; -fx-alignment: center; -fx-font-size: .5cm;");
        Text footerText = new Text("Copyright (c) 2018 Christopher Soderquist");
        footerText.setFill(Color.WHITE);
        footer.getChildren().add(footerText);

        Scene scene = new Scene(mainLayout, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
